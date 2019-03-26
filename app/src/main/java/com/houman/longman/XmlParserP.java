package com.houman.longman;

import com.houman.longman.Models.xmlModle.Entery;
import com.houman.longman.Models.xmlModle.Head.Head;
import com.houman.longman.Models.xmlModle.Head.LEVEL;
import com.houman.longman.Models.xmlModle.Sense.ColloExa;
import com.houman.longman.Models.xmlModle.Sense.EXAMPLE;
import com.houman.longman.Models.xmlModle.Sense.ExpandableInformation;
import com.houman.longman.Models.xmlModle.Sense.Sense;
import com.houman.longman.Models.xmlModle.Sense.SpokenSect;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class XmlParserP
    {

    private String HWDb = "";

    Entery pars(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        int eventType = parser.getEventType();
        List<Sense> mSense = new ArrayList<>();
        Head mHead = null;
        SpokenSect mSpokenSect = null;

        while (eventType != XmlPullParser.END_DOCUMENT)
            {
            switch (eventType)
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "Sense":
                            mSense.add(getSense(parser)); //todo
                            break;
                        case "PhrVbEntry":
                            getPhrVbEntry(parser);
                            break;
                        case "SpokenSect":
                            mSpokenSect = getSpokenSect(parser); //todo
                            break;
                        case "Head":
                            mHead = getHead(parser);
                            break;
                        }
                    break;
                }
            eventType = parser.next();
            }
        return new Entery(mHead,mSense,mSpokenSect);
        }

    private void getPhrVbEntry(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        boolean endTag = true;

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "HWD":
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("PhrVbEntry"))
                        endTag = false;
                    break;
                }
            }
        }

    private SpokenSect getSpokenSect(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        boolean endTag = true;
        List<Sense> mSense = new ArrayList<>();

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("Sense"))
                        mSense.add(getSense(parser));
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("SpokenSect"))
                        endTag = false;
                    break;
                }
            }
        return new SpokenSect(mSense);
        }

    private Head getHead(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        boolean endTag = true;

        String HWD = "";
        String HYPHENATION = "";
        String HOMNUM = "";
        String POS = "";
        String GRAM = "";
        String REGISTERLAB = "";
        StringBuilder PronCodes = new StringBuilder();
        LEVEL LEVELs = null;
        List<String> FRQ = new ArrayList<>();
        List<String> SearchInflections = new ArrayList<>();

        /*
        StringBuilder mBuffer = new StringBuilder();
        mBuffer.append("<strong><big><font color='red'>");
        mBuffer.append(Word);
        mBuffer.append(" ");
        mBuffer.append(head.getHOMNUM());
        mBuffer.append(" </font></big></strong>");
        mBuffer.append(head.getPronCodes());
        mBuffer.append("<br><strong><big><a href='");
        mBuffer.append(id);
        mBuffer.append("'>&#128266;</a></big></strong>");
         */

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "HWD":
                            HWD = parser.nextText();
                            HWDb = HWD;
                            break;
                        case "HOMNUM":
                            HOMNUM = parser.nextText();
                            break;
                        case "HYPHENATION":
                            HYPHENATION = parser.nextText();
                            break;
                        case "REGISTERLAB":
                            REGISTERLAB = parser.nextText();
                            PronCodes.append(" <em>").append(REGISTERLAB).append("</em>");
                            REGISTERLAB = "";
                            break;
                        case "PronCodes":
                            PronCodes.append(getPronCodes(parser));
                            break;
                        case "LEVEL":
                            String pron, value;
                            pron = parser.getAttributeValue(null, "pron");
                            value = parser.getAttributeValue(null, "value");
                            LEVELs = new LEVEL(pron, value);
                            break;
                        case "FREQ":
                            FRQ.add(parser.nextText());
                            break;
                        case "POS":
                            String buf = parser.nextText();
                            switch (buf)
                                {
                                case "v":
                                    POS = "verb";
                                    break;
                                case "n":
                                    POS = "noun";
                                    break;
                                default:
                                    POS = buf;
                                }
                            PronCodes.append(" <span style='color: #33cccc;'><em>").append(POS).append("</em></span>");
                            POS = "";
                            break;
                        case "GRAM":
                            String StringB = parser.nextText();
                            StringB = StringB.replace("I","intransitive");
                            StringB = StringB.replace("T","transitive");
                            StringB = StringB.replace("C","countable");
                            StringB = StringB.replace("U","uncountable");
                            StringB = StringB.replace(",",", ");
                            PronCodes.append("<br><span style='color: #c88719;'>[").append(StringB).append("]</span>");
                            break;
                        case "Inflections":
                            PronCodes.append("<br>(").append(getInflections(parser)).append(")");
                            break;
                        case "SearchInflections":
                            SearchInflections.addAll(getSearchInflections(parser));
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Head"))
                        endTag = false;
                    break;
                }
            }
        return new Head(HWD,HYPHENATION,HOMNUM, PronCodes.toString(),LEVELs,FRQ, SearchInflections);
        }

    private String getInflections(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        boolean endTag = true;
        boolean pFlag = true;
        StringBuilder Result = new StringBuilder();

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "PASTTENSE":
                            Result.append("past tense <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "PASTPART":
                            Result.append(" past participle <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "PRESPART":
                            Result.append(" present participle <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "T3PERSSING":
                            Result.append(" third person singular <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "PTandPPX":
                            Result.append("past tense and past participle <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "PRESPARTX":
                            Result.append(" present participle <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "T3PERSSINGX":
                            Result.append(" third person singular <strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "PLURALFORM":
                            if (pFlag)
                                {
                                pFlag = false;
                                Result.append("plural  <strong>").append(parser.nextText()).append("</strong>");
                                }
                            else
                                Result.append(", <strong>").append(parser.nextText()).append(
                                        "</strong>");
                            break;
                        case "PronCodes":
                            Result.append(getPronCodes(parser));
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Inflections"))
                        endTag = false;
                    break;
                }
            }
        return Result.toString();
        }

    private List<String> getSearchInflections(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        List<String> Result = new ArrayList<>();
        boolean endTag = true;

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "WF":
                            Result.add(parser.nextText());
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("SearchInflections"))
                        endTag = false;
                    break;
                }
            }
        return  Result;
        }

    private String getPronCodes(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        boolean endTag = true;
        boolean AmFlag = true;
        boolean BreFlag = true;

        StringBuilder PRON = new StringBuilder();
        PRON.append(" /");

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "STRONG":
                            PRON.append("; <strong>").append(parser.nextText()).append("</strong>");
                            break;
                        case "i":
                            PRON.append("<em>").append(parser.nextText()).append("</em>");
                            break;
                        case "AMEVARPRON":
                            if (AmFlag)
                                {
                                AmFlag = false;
                                PRON.append(" $ ");
                                }
                            PRON.append(parser.nextText());
                            break;
                        }
                    break;
                case XmlPullParser.TEXT:
                        if (BreFlag)
                            PRON.append(parser.getText());
                        else
                            PRON.append(" ").append(parser.getText());
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("PRON"))
                        BreFlag = false;
                    if (parser.getName().equals("PronCodes"))
                        endTag = false;
                    break;
                }
            }
        PRON.append("/");
        return PRON.toString().replace("\n","").replace(" ","");
        }

    private Sense getSense(XmlPullParser parser) throws XmlPullParserException, IOException
        {
        boolean endTag = true;
        String Definition;
        ExpandableInformation Expandable = null;
        String Id = parser.getAttributeValue(null, "id");
        StringBuilder buff = new StringBuilder();

        while (endTag)//todo pars subsense
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "SIGNPOST":
                            buff.append("<big><span style=\"color: #4fb3bf;\"><strong>").append(parser.nextText()).append("</strong></span></big> ");
                            break;
                        case "GRAM":
                            String StringB = parser.nextText();
                            StringB = StringB.replace("I","intransitive");
                            StringB = StringB.replace("T","transitive");
                            StringB = StringB.replace("adv","adverb");
                            StringB = StringB.replace("adj","adjective");
                            StringB = StringB.replace("prep","preposition");
                            StringB = StringB.replace("C","countable");
                            StringB = StringB.replace("U","uncountable");
                            StringB = StringB.replace(",",", ");
                            buff.append("<span style=\"color: #c88719;\">[").append(StringB).append("]</span> ");
                            break;
                        case "DEF":
                            buff.append(getDefinition(parser));
                            break;
                        case "ExpandableInformation":
                            Expandable = getExpandable(parser);
                            break;
                        case "LEXUNIT":
                            buff.append("<big><strong>").append(parser.nextText()).append(
                                    "</strong></big> ");
                            break;
                        case "Variant":
                            buff.append(getVariant(parser));
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Sense"))
                        endTag = false;
                    break;
                }
            }
        Definition = buff.toString();
        Definition = Definition.replace("sth","something");
        Definition = Definition.replace("sb","somebody");

        return new Sense(Id, Definition,  Expandable,0);
        }

    private String getDefinition(XmlPullParser parser) throws IOException, XmlPullParserException
        {
        boolean endTag = true;
        boolean RefhwdFlag = false;
        StringBuilder Definition = new StringBuilder();
        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "DEF":
                            break;
                        case "REFHWD":
                            RefhwdFlag = true;
                            break;
                        }
                    break;
                case XmlPullParser.TEXT:
                    String buffer = parser.getText();
                    if (RefhwdFlag)
                        buffer = buffer.toUpperCase();
                    Definition.append(buffer);
                    break;
                case XmlPullParser.END_TAG:
                    switch (parser.getName())
                        {
                        case "DEF":
                            endTag = false;
                            break;
                        case "REFHWD":
                            RefhwdFlag = false;
                            break;
                        }
                    break;
                }
            }
        return Definition.toString();
        }

    private String getVariant(XmlPullParser parser) throws IOException, XmlPullParserException
        {
        boolean endTag = true;
        StringBuilder buff = new StringBuilder();

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "LINKWORD":
                            buff.append("(").append(parser.nextText());
                            break;
                        case "LEXVAR":
                            buff.append(" <strong>").append(parser.nextText()).append("</strong>) ");
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("Variant"))
                        endTag = false;
                    break;
                }
            }
        return buff.toString();
        }

    private ExpandableInformation getExpandable(XmlPullParser parser) throws IOException, XmlPullParserException
        {
        boolean endTag = true;
        List<EXAMPLE> oneEXAMPLE = new ArrayList<>();
        List<EXAMPLE> GResult = new ArrayList<>();
        List<ColloExa> CResult = new ArrayList<>();

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "EXAMPLE":
                            oneEXAMPLE.add(getEXAMPLE(parser));
                            break;
                        case "GramExa":
                            oneEXAMPLE.add(getGramExa(parser));
                            break;
                        case "ColloExa":
                            CResult.add(getColloExa(parser));
                            break;
                        }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("ExpandableInformation"))
                        endTag = false;
                    break;
                }
            }
        return new ExpandableInformation(GResult,CResult,oneEXAMPLE);
        }

    private EXAMPLE getEXAMPLE(XmlPullParser parser) throws IOException, XmlPullParserException
        {
        boolean endTag = true;
        boolean CFlag = false;
        StringBuilder mExample = new StringBuilder();
        String Id = parser.getAttributeValue(null, "id");
        mExample.append("<big><a href='");
        mExample.append(Id);
        mExample.append("'>&#128266;</a></big> <em>");
        String buf;
        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "EXAMPLE":
                            CFlag = false;
                            break;
                        case "COLLOINEXA":
                            CFlag = true;
                            break;
                        }
                    break;
                case XmlPullParser.TEXT:
                    if(CFlag)
                        {
                        mExample.append("<strong>").append(parser.getText()).append("</strong>");
                        CFlag = false;
                        }
                    else
                        mExample.append(parser.getText());

                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("EXAMPLE"))
                        endTag = false;
                    break;
                }
            }
        mExample.append("</em>");
        buf = mExample.toString();
//        buf = buf.replace("sth","something");
//        buf = buf.replace("sb","somebody");
        return new EXAMPLE(Id, buf);
        }

    private ColloExa getColloExa(XmlPullParser parser) throws IOException, XmlPullParserException
        {
        boolean endTag = true;
        String COLLO = "";
        List<EXAMPLE> oneEXAMPLE = new ArrayList<>();
        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "COLLO":
                            COLLO = parser.nextText();
                            break;
                        case "EXAMPLE":
                            oneEXAMPLE.add(getEXAMPLE(parser));
                            break;
                        }
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("ColloExa"))
                        endTag = false;
                    break;
                }
            }
        return new ColloExa(COLLO,oneEXAMPLE);
        }

    private EXAMPLE getGramExa(XmlPullParser parser) throws IOException, XmlPullParserException
        {
        boolean endTag = true;
        String ID = "";
        StringBuilder buff = new StringBuilder();
        String Result;

        while (endTag)
            {
            parser.next();
            switch (parser.getEventType())
                {
                case XmlPullParser.START_TAG:
                    switch (parser.getName())
                        {
                        case "PROPFORM":
                            buff.append("<strong>").append(parser.nextText()).append(
                                    "</strong>");
                            break;
                        case "GLOSS":
                            buff.append("(=").append(parser.nextText()).append(")");
                            break;
                        case "PROPFORMPREP":
                            buff.append("<strong>").append(HWDb).append(" ").append(parser.nextText()).append("</strong>");
                            break;
                        case "EXAMPLE":
                            EXAMPLE bE = getEXAMPLE(parser);
                            buff.append("<br>").append(bE.getEXAMPLEs());
                            ID = bE.getId();
                            break;
                        }
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("GramExa"))
                        endTag = false;
                    break;
                }
            }
        Result = buff.toString();
        Result = Result.replace("sth","something");
        Result = Result.replace("sb","somebody");
        return new EXAMPLE(ID,Result);
        }
    }
