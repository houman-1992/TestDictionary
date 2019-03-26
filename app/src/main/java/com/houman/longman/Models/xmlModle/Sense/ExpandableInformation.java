package com.houman.longman.Models.xmlModle.Sense;

import java.util.List;

public class ExpandableInformation
    {
    private List<EXAMPLE> GramExas;
    private List<ColloExa> ColloExas;
    private List<EXAMPLE> EXAMPLEs;

    public ExpandableInformation(List<EXAMPLE> gramExas, List<ColloExa> colloExas, List<EXAMPLE> EXAMPLEs)
        {
        this.GramExas = gramExas;
        this.ColloExas = colloExas;
        this.EXAMPLEs = EXAMPLEs;
        }

    public List<ColloExa> getColloExas()
        {
        return ColloExas;
        }

    public void setColloExas(List<ColloExa> colloExas)
        {
        ColloExas = colloExas;
        }

    public List<EXAMPLE> getGramExas()
        {
        return GramExas;
        }

    public void setGramExas(List<EXAMPLE> gramExas)
        {
        GramExas = gramExas;
        }

    public List<EXAMPLE> getEXAMPLEs()
        {
        return EXAMPLEs;
        }

    public void setEXAMPLEs(List<EXAMPLE> EXAMPLEs)
        {
        this.EXAMPLEs = EXAMPLEs;
        }
    }
