/****************************************************************************************************
*                                                                                                   *
*                   IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.               *
*                                                                                                   *
*   By downloading, copying, installing or using the software you agree to this license.            *
*   If you do not agree to this license, do not download, install, copy or use the software.        *
*                                                                                                   *
*                                   License Agreement                                               *
*                           For Vision Open Statistical Models 2D Explorer                          *
*                                                                                                   *
* Copyright (C):    2006~2014 by JIA Pei, all rights reserved.                                      *
*                                                                                                   *
*                   VOSM2DExplorer is free software under the terms of the GNU Lesser General       *
*                   Public License (GNU LGPL) as published by the Free Software Foundation;         *
*                   either version 3.0 of the License, or (at your option) any later version.       *
*                                                                                                   *
*                   You can use it, modify it, redistribute it, etc; and redistribution and use in  *
*                   source and binary forms, with or without modification, are permitted provided   *
*                   that the following conditions are met:                                          *
*                                                                                                   *
*                   a) Redistribution's of source code must retain this whole paragraph of          *
*                   copyright notice, including this list of conditions and all the following       *
*                   contents in this copyright paragraph.                                           *
*                                                                                                   *
*                   b) Redistribution's in binary form must reproduce this whole paragraph of       *
*                   copyright notice, including this list of conditions and all the following       *
*                   contents in this copyright paragraph, and/or other materials provided with      *
*                   the distribution.                                                               *
*                                                                                                   *
*                   c) The name of the copyright holders may not be used to endorse or promote      *
*                   products derived from this software without specific prior written permission.  *
*                                                                                                   *
*                   Any publications based on this code must cite the following five papers,        *
*                   technic reports and on-line materials.                                          *
*                                                                                                   *
*                   1) P. JIA, 2D Statistical Models, Technical Report of Vision Open Working       *
*                   Group, 2st Edition, Oct 21, 2010.                                               *
*                   http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf    *
*                                                                                                   *
*                   2) P. JIA. Audio-visual based HMI for an Intelligent Wheelchair.                *
*                   PhD thesis, University of Essex, 2010.                                          *
*                   http://www.visionopen.com/members/jiapei/publications/pei_thesischapter34.pdf   *
*                                                                                                   *
* Version:          1.0                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jp4work@gmail.com                                                               *
* URL:              http://www.visionopen.com                                                       *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.smbasic;

public class VO_FacePart {
	/** type */
	private PartType		m_iType;
	
	/** open or closed */
	private boolean			m_bClosed;
	
	/** index vector */
	private int[]			m_vIndexes;

	private void			init()
    {
                            this.m_iType = PartType.NONE;			// actually, type 0 is just a default value, which is not used at all
                            this.m_bClosed = false;
                            this.m_vIndexes = null;
    }

    private void			CopyData(final VO_FacePart iFacePart)
    {
                            this.m_iType 			= iFacePart.GetType();
                            this.m_bClosed 			= iFacePart.IsClosedOrNot();
                            this.m_vIndexes			= new int [ iFacePart.GetIndexes().length ];
                            java.lang.System.arraycopy(iFacePart.GetIndexes(), 0, this.m_vIndexes, 0, this.m_vIndexes.length);
    }

    public enum PartType  { NONE, WHOLEFACE, CHIN, LEFTEYEBROW, RIGHTEYEBROW,
							LEFTEYE, RIGHTEYE, NOSE, NOSTRIL, NOSETIP,
							LIPOUTERLINE, LIPINNERLINE, LIPUPPEROUTERLINE, LIPUPPERINNERLINE,
							LIPDOWNEROUTERLINE, LIPDOWNERINNERLINE, LEFTSIDEPOINTS,
							RIGHTSIDEPOINTS, MIDLINEPOINTS, EYECORNERPOINTS,
							MOUTHCORNERPOINTS, PITCHAXISLINEPOINTS, LEFTIRIS, RIGHTIRIS,
							LEFTEAR, RIGHTEAR, WHOLEMOUTH, NOSECENTRALAREA};

    /** Default constructor to create a VO_FaceParts object */
	public VO_FacePart() 	{ this.init(); }

    /** Copy constructor */
	public VO_FacePart( final VO_FacePart iFacePart )
    {
							this.CopyData(iFacePart);
    }

    /** operator= overloading, similar to copy constructor */
	public VO_FacePart clone (final VO_FacePart iFacePart)
    {
							this.CopyData(iFacePart);
							return this;
    }

	public void				clear() { this.init();}

	public PartType			GetType() {return this.m_iType;}
	public boolean			IsClosedOrNot() {return this.m_bClosed;}
	public int[]			GetIndexes() {return this.m_vIndexes;}

	public void				SetType(final PartType type) {this.m_iType = type;}
	public void				SetClosedOrNot(boolean close) {this.m_bClosed = close;}
	public void				SetIndexes(final int[] indexes) {this.m_vIndexes = indexes;}
	
	public void				ReadFacePart(final String str) {
							String ss[] = str.split(" ");
							
						    if(ss[0].compareTo("m_vChin") == 0)
						    {
						        this.m_iType = PartType.CHIN;
						    }
						    else if(ss[0].compareTo("m_vLeftEyebrow") == 0)
						    {
						    	this.m_iType = PartType.LEFTEYEBROW;
						    }
						    else if(ss[0].compareTo("m_vRightEyebrow") == 0)
						    {
						    	this.m_iType = PartType.RIGHTEYEBROW;
						    }
						    else if(ss[0].compareTo("m_vLeftEye") == 0)
						    {
						    	this.m_iType = PartType.LEFTEYE;
						    }
						    else if(ss[0].compareTo("m_vRightEye") == 0)
						    {
						    	this.m_iType = PartType.RIGHTEYE;
						    }
						    else if(ss[0].compareTo("m_vNose") == 0)
						    {
						        this.m_iType = PartType.NOSE;
						    }
						    else if(ss[0].compareTo("m_vNostril") == 0)
						    {
						        this.m_iType = PartType.NOSTRIL;
						    }
						    else if(ss[0].compareTo("m_vNoseTip") == 0)
						    {
						        this.m_iType = PartType.NOSETIP;
						    }
						    else if(ss[0].compareTo("m_vLipOuterLine") == 0)
						    {
						        this.m_iType = PartType.LIPOUTERLINE;
						    }
						    else if(ss[0].compareTo("m_vLipInnerLine") == 0)
						    {
						        this.m_iType = PartType.LIPINNERLINE;
						    }
						    else if(ss[0].compareTo("m_vLipUpperOuterLine") == 0)
						    {
						        this.m_iType = PartType.LIPUPPEROUTERLINE;
						    }
						    else if(ss[0].compareTo("m_vLipUpperInnerLine") == 0)
						    {
						        this.m_iType = PartType.LIPUPPERINNERLINE;
						    }
						    else if(ss[0].compareTo("m_vLipDownerOuterLine") == 0)
						    {
						        this.m_iType = PartType.LIPDOWNEROUTERLINE;
						    }
						    else if(ss[0].compareTo("m_vLipDownerInnerLine") == 0)
						    {
						        this.m_iType = PartType.LIPDOWNERINNERLINE;
						    }
						    else if(ss[0].compareTo("m_vLeftSidePoints") == 0)
						    {
						        this.m_iType = PartType.LEFTSIDEPOINTS;
						    }
						    else if(ss[0].compareTo("m_vRightSidePoints") == 0)
						    {
						        this.m_iType = PartType.RIGHTSIDEPOINTS;
						    }
						    else if(ss[0].compareTo("m_vMidlinePoints") == 0)
						    {
						        this.m_iType = PartType.MIDLINEPOINTS;
						    }
						    else if(ss[0].compareTo("m_vEyeCornerPoints") == 0)
						    {
						        this.m_iType = PartType.EYECORNERPOINTS;
						    }
						    else if(ss[0].compareTo("m_vMouthCornerPoints") == 0)
						    {
						        this.m_iType = PartType.MOUTHCORNERPOINTS;
						    }
						    else if(ss[0].compareTo("m_vPitchAxisLinePoints") == 0)
						    {
						        this.m_iType = PartType.PITCHAXISLINEPOINTS;
						    }
						    else if(ss[0].compareTo("m_vLeftIris") == 0)
						    {
						        this.m_iType = PartType.LEFTIRIS;
						    }
						    else if(ss[0].compareTo("m_vRightIris") == 0)
						    {
						        this.m_iType = PartType.RIGHTIRIS;
						    }
						    else if(ss[0].compareTo("m_vLeftEar") == 0)
						    {
						        this.m_iType = PartType.LEFTEAR;
						    }
						    else if(ss[0].compareTo("m_vRightEar") == 0)
						    {
						        this.m_iType = PartType.RIGHTEAR;
						    }

						    if(ss[1].compareTo("open") == 0)
						        this.m_bClosed = false;
						    else if(ss[1].compareTo("closed") == 0)
						        this.m_bClosed = true;

						    this.m_vIndexes = new int[ss.length-2];
						    for(int i = 2; i < ss.length; i++)
				        	{
						    	this.m_vIndexes[i-2] = Integer.parseInt(ss[i]);
				        	}
	}
}
