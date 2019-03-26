package com.houman.longman.Models.xmlModle.Sense;

public class Variant
    {
    private String LINKWORD;
    private String LEXVAR;

    public Variant(String LINKWORD, String LEXVAR)
        {
        this.LINKWORD = LINKWORD;
        this.LEXVAR = LEXVAR;
        }

    public String getLINKWORD()
        {
        return LINKWORD;
        }

    public void setLINKWORD(String LINKWORD)
        {
        this.LINKWORD = LINKWORD;
        }

    public String getLEXVAR()
        {
        return LEXVAR;
        }

    public void setLEXVAR(String LEXVAR)
        {
        this.LEXVAR = LEXVAR;
        }
    }
