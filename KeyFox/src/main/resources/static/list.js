var myChart = echarts.init(document.getElementById('main'));

$.get('dataJson').done(function (data) {
  console.info(data);
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
      data: ['UDP', 'IPV4', 'TCP', 'IPV6']
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
    series: [
      {
        name: 'UDP',
        type: 'line',
        stack: '总量',
        areaStyle: {normal: {}},
        data: data.UDP
      },
      {
        name: 'IPV4',
        type: 'line',
        stack: '总量',
        areaStyle: {normal: {}},
        data: data.IPV4
      },
      {
        name: 'TCP',
        type: 'line',
        stack: '总量',
        areaStyle: {normal: {}},
        data: data.TCP
      },
      {
        name: 'IPV6',
        type: 'line',
        stack: '总量',
        areaStyle: {normal: {}},
        data: data.IPV6
      }
    ]
  });
});
