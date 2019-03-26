package com.houman.longman.Models.xmlModle.Head;

public class PronCode
    {
    private String PRON;
    private String AMEVARPRON;
    private String STRONG;

    public PronCode(String PRON, String AMEVARPRON, String STRONG)
        {
        this.PRON = PRON;
        this.AMEVARPRON = AMEVARPRON;
        this.STRONG = STRONG;
        }

    public String getSTRONG()
        {
        return STRONG;
        }

    public void setSTRONG(String STRONG)
        {
        this.STRONG = STRONG;
        }

    public String getPRON()
        {
        return PRON;
        }

    public void setPRON(String PRON)
        {
        this.PRON = PRON;
        }

    public String getAMEVARPRON()
        {
        return AMEVARPRON;
        }

    public void setAMEVARPRON(String AMEVARPRON)
        {
        this.AMEVARPRON = AMEVARPRON;
        }
    }
