package com.houman.longman.Models.xmlModle.Head;

public class LEVEL
    {
    private String pron;
    private String value;

    public LEVEL(String pron, String value)
        {
        this.pron = pron;
        this.value = value;
        }

    public String getPron()
        {
        return pron;
        }

    public void setPron(String pron)
        {
        this.pron = pron;
        }

    public String getValue()
        {
        return value;
        }

    public void setValue(String value)
        {
        this.value = value;
        }
    }
