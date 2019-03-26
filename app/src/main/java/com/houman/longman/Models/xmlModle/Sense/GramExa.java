package com.houman.longman.Models.xmlModle.Sense;

import java.util.List;

public class GramExa
    {
    private String PROPFORM;
    private List<EXAMPLE> EXAMPLEs;
    private String GLOSS;
    private String PROPFORMPREP;

    public GramExa(String PROPFORM, List<EXAMPLE> EXAMPLEs, String GLOSS, String PROPFORMPREP)
        {
        this.PROPFORM = PROPFORM;
        this.EXAMPLEs = EXAMPLEs;
        this.GLOSS = GLOSS;
        this.PROPFORMPREP = PROPFORMPREP;
        }

    public String getPROPFORM()
        {
        return PROPFORM;
        }

    public void setPROPFORM(String PROPFORM)
        {
        this.PROPFORM = PROPFORM;
        }

    public List<EXAMPLE> getEXAMPLEs()
        {
        return EXAMPLEs;
        }

    public void setEXAMPLEs(List<EXAMPLE> EXAMPLEs)
        {
        this.EXAMPLEs = EXAMPLEs;
        }

    public String getGLOSS()
        {
        return GLOSS;
        }

    public void setGLOSS(String GLOSS)
        {
        this.GLOSS = GLOSS;
        }

    public String getPROPFORMPREP()
        {
        return PROPFORMPREP;
        }

    public void setPROPFORMPREP(String PROPFORMPREP)
        {
        this.PROPFORMPREP = PROPFORMPREP;
        }
    }
