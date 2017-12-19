package com.keyfox.entity;

/**
 * Created by Administrator on 2017/12/19.
 */
public class LineDiagramEntity {

    private Object[] UDP;
    private Object[] IPV4;
    private Object[] TCP;
    private Object[] IPV6;

    public Object[] getUDP() {
        return UDP;
    }

    public void setUDP(Object[] UDP) {
        this.UDP = UDP;
    }

    public Object[] getIPV4() {
        return IPV4;
    }

    public void setIPV4(Object[] IPV4) {
        this.IPV4 = IPV4;
    }

    public Object[] getTCP() {
        return TCP;
    }

    public void setTCP(Object[] TCP) {
        this.TCP = TCP;
    }

    public Object[] getIPV6() {
        return IPV6;
    }

    public void setIPV6(Object[] IPV6) {
        this.IPV6 = IPV6;
    }
}
