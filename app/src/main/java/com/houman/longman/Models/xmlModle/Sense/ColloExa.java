package com.houman.longman.Models.xmlModle.Sense;

import java.util.List;

public class ColloExa
    {
    private String COLLO;
    private List<EXAMPLE> EXAMPLEs;

    public ColloExa(String COLLO, List<EXAMPLE> EXAMPLEs)
        {
        this.COLLO = COLLO;
        this.EXAMPLEs = EXAMPLEs;
        }

    public List<EXAMPLE> getEXAMPLEs()
        {
        return EXAMPLEs;
        }

    public void setEXAMPLEs(List<EXAMPLE> EXAMPLEs)
        {
        this.EXAMPLEs = EXAMPLEs;
        }

    public String getCOLLO()
        {
        return COLLO;
        }

    public void setCOLLO(String COLLO)
        {
        this.COLLO = COLLO;
        }

    }
