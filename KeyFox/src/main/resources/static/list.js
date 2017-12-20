var myChart = echarts.init(document.getElementById('main'));

$.get('dataJsonLine').done(function (data) {
  myChart.setOption({
    title: {
      text: '流量信息'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      data: data
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'time'
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: 'MbPS'
      }
    ],
    series: data
  });
});


var heliuMain = echarts.init(document.getElementById('heliuMain'));

$.get('dataJsonThemeRiver').done(function (data) {
  console.log(data);
  heliuMain.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'line',
        lineStyle: {
          color: 'rgba(0,0,0,0.2)',
          width: 1,
          type: 'solid'
        }
      }
    },



    singleAxis: {
      top: 50,
      bottom: 50,
      axisTick: {},
      axisLabel: {},
      type: 'time',
      axisPointer: {
        animation: true,
        label: {
          show: true
        }
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed',
          opacity: 0.2
        }
      }
    },

    series: data
  });
});

