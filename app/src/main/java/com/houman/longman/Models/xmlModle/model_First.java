package com.houman.longman.Models.xmlModle;

public class model_First
    {
    private String HWD;
    private String Core;

    public model_First(String HWD, String core)
        {
        this.HWD = HWD;
        Core = core;
        }

    public String getHWD()
        {
        return HWD;
        }

    public void setHWD(String HWD)
        {
        this.HWD = HWD;
        }

    public String getCore()
        {
        return Core;
        }

    public void setCore(String core)
        {
        Core = core;
        }
    }
