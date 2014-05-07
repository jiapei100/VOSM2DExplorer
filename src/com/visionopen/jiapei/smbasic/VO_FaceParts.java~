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
*                   VOSM is free software under the terms of the GNU Lesser General Public License  *
*                   (GNU LGPL) as published by the Free Software Foundation; either version 3.0 of  *
*                   the License, or (at your option) any later version.                             *
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
*                                                                                                   *                                                                                                   *
* Version:          1.0                                                                           	*
* Author:           JIA Pei                                                                         *
* Contact:          jp4work@gmail.com                                                               *
* URL:              http://www.visionopen.com                                                       *
* Create Date:      2010-11-04                                                                      *
* Modify Date:      2014-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.smbasic;


public class VO_FaceParts {
    /** Chin indexes */
	private VO_FacePart						m_vChin;

    /** LeftEyebrow indexes */
	private VO_FacePart						m_vLeftEyebrow;

    /** RightEyebrow indexes */
	private VO_FacePart						m_vRightEyebrow;

    /** LeftEye indexes */
	private VO_FacePart						m_vLeftEye;

    /** RightEye indexes */
	private VO_FacePart						m_vRightEye;

    /** Nose indexes */
	private VO_FacePart						m_vNose;

    /** Nostril indexes */
	private VO_FacePart						m_vNostril;

    /** Nose Tip indexes */
	private VO_FacePart						m_vNoseTip;

    /** Lip outerline indexes */
	private VO_FacePart						m_vLipOuterLine;

    /** Lip innerline indexes */
	private VO_FacePart						m_vLipInnerLine;

    /** Lip upper outer bound indexes */
	private VO_FacePart						m_vLipUpperOuterLine;

    /** Lip upper inner bound indexes */
	private VO_FacePart						m_vLipUpperInnerLine;

    /** Lip downer outer bound indexes */
	private VO_FacePart						m_vLipLowerOuterLine;

    /** Lip downer inner bound indexes */
	private VO_FacePart						m_vLipLowerInnerLine;

    /** Left Side Points indexes */
	private VO_FacePart						m_vLeftSidePoints;

    /** Right Side Points indexes */
	private VO_FacePart						m_vRightSidePoints;

    /** Midline Points indexes */
	private VO_FacePart						m_vMidlinePoints;

    /** Eyes' Corners' Points indexes */
	private VO_FacePart						m_vEyeCornerPoints;

    /** Mouth Corners' Points indexes */
	private VO_FacePart						m_vMouthCornerPoints;

    /** Pitch Axis Points indexes */
	private VO_FacePart						m_vPitchAxisLinePoints;

    /** Left Iris indexes */
	private VO_FacePart						m_vLeftIris;

    /** Right Iris indexes */
	private VO_FacePart						m_vRightIris;

    /** Left Ear indexes */
	private VO_FacePart						m_vLeftEar;

    /** Right Ear indexes */
	private VO_FacePart						m_vRightEar;
	
	/** Triangles' indexes. NbOfTriangles*3 */
	private int[][]							m_vvTriangleIndexes;

	private void							init()
    {
											this.m_vChin				= null;
											this.m_vLeftEyebrow			= null;
											this.m_vRightEyebrow		= null;
											this.m_vLeftEye				= null;
											this.m_vRightEye			= null;
											this.m_vNose				= null;
											this.m_vNostril				= null;
											this.m_vNoseTip				= null;
											this.m_vLipOuterLine		= null;
											this.m_vLipInnerLine		= null;
											this.m_vLipUpperOuterLine	= null;
											this.m_vLipUpperInnerLine	= null;
											this.m_vLipLowerOuterLine	= null;
											this.m_vLipLowerInnerLine	= null;
											this.m_vLeftSidePoints		= null;
											this.m_vRightSidePoints		= null;
											this.m_vMidlinePoints		= null;
											this.m_vEyeCornerPoints		= null;
											this.m_vMouthCornerPoints	= null;
											this.m_vPitchAxisLinePoints	= null;
											this.m_vLeftIris			= null;
											this.m_vRightIris			= null;
											this.m_vLeftEar				= null;
											this.m_vRightEar			= null;
											this.m_vvTriangleIndexes	= null;
    }

    private void							CopyData(final VO_FaceParts iFaceParts)
    {
											this.m_vChin 					= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.CHIN);
											this.m_vLeftEyebrow 			= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LEFTEYEBROW);
											this.m_vRightEyebrow			= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.RIGHTEYEBROW);
											this.m_vLeftEye 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LEFTEYE);
											this.m_vRightEye 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.RIGHTEYE);
											this.m_vNose 					= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.NOSE);
											this.m_vNostril 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.NOSTRIL);
											this.m_vNoseTip					= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.NOSETIP);
											this.m_vLipOuterLine 			= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LIPOUTERLINE);
											this.m_vLipInnerLine 			= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LIPINNERLINE);
											this.m_vLipUpperOuterLine		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LIPUPPEROUTERLINE);
											this.m_vLipUpperInnerLine		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LIPUPPERINNERLINE);
											this.m_vLipLowerOuterLine		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LIPDOWNEROUTERLINE);
											this.m_vLipLowerInnerLine		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LIPDOWNERINNERLINE);
											this.m_vLeftSidePoints 			= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LEFTSIDEPOINTS);
											this.m_vRightSidePoints 		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.RIGHTSIDEPOINTS);
											this.m_vMidlinePoints 			= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.MIDLINEPOINTS);
											this.m_vEyeCornerPoints 		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.EYECORNERPOINTS);
											this.m_vMouthCornerPoints		= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.MOUTHCORNERPOINTS);
											this.m_vPitchAxisLinePoints    	= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.PITCHAXISLINEPOINTS);
											this.m_vLeftIris 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LEFTIRIS);
											this.m_vRightIris 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.RIGHTIRIS);
											this.m_vLeftEar 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.LEFTEAR);
											this.m_vRightEar 				= iFaceParts.VO_GetOneFacePart(VO_FacePart.PartType.RIGHTEAR);
											this.m_vvTriangleIndexes		= iFaceParts.GetTriangleIndexes();
    }

    /** Default constructor to create a VO_FaceParts object */
    public VO_FaceParts() 					{ this.init(); }

    /** Copy constructor */
    public VO_FaceParts( final VO_FaceParts iFaceParts )	{ this.CopyData(iFaceParts); }

    /** operator= overloading, similar to copy constructor */
    public VO_FaceParts clone(final VO_FaceParts iFaceParts)
    {
											this.CopyData(iFaceParts);
											return this;
    }

    public void clear() 					{ this.init(); }

    public VO_FacePart         				VO_GetOneFacePart( VO_FacePart.PartType partIdx)
    {
    	switch(partIdx )
        {
		case WHOLEFACE:
			break;
    	case CHIN:
            return this.m_vChin;
        case LEFTEYEBROW:
            return this.m_vLeftEyebrow;
        case RIGHTEYEBROW:
            return this.m_vRightEyebrow;
        case LEFTEYE:
            return this.m_vLeftEye;
        case RIGHTEYE:
            return this.m_vRightEye;
        case NOSE:
            return this.m_vNose;
        case NOSTRIL:
            return this.m_vNostril;
        case NOSETIP:
            return this.m_vNoseTip;
        case LIPOUTERLINE:
            return this.m_vLipOuterLine;
        case LIPINNERLINE:
            return this.m_vLipInnerLine;
        case LIPUPPEROUTERLINE:
            return this.m_vLipUpperOuterLine;
        case LIPUPPERINNERLINE:
            return this.m_vLipUpperInnerLine;
        case LIPDOWNEROUTERLINE:
            return this.m_vLipLowerOuterLine;
        case LIPDOWNERINNERLINE:
            return this.m_vLipLowerInnerLine;
        case LEFTSIDEPOINTS:
            return this.m_vLeftSidePoints;
        case RIGHTSIDEPOINTS:
            return this.m_vRightSidePoints;
        case MIDLINEPOINTS:
            return this.m_vMidlinePoints;
        case EYECORNERPOINTS:
            return this.m_vEyeCornerPoints;
        case MOUTHCORNERPOINTS:
            return this.m_vMouthCornerPoints;
        case PITCHAXISLINEPOINTS:
            return this.m_vPitchAxisLinePoints;
        case LEFTIRIS:
            return this.m_vLeftIris;
        case RIGHTIRIS:
            return this.m_vRightIris;
        case LEFTEAR:
            return this.m_vLeftEar;
        case RIGHTEAR:
            return this.m_vRightEar;
		case WHOLEMOUTH:
			break;
		case NOSECENTRALAREA:
			break;
		case NONE:
			break;
    	}
		return null;
    }

	public void         					VO_SetOneFacePart(final VO_FacePart iFacePart)
	{
	    switch(iFacePart.GetType() )
	    {
	    case CHIN:
	        this.m_vChin = iFacePart;
	        break;
	    case LEFTEYEBROW:
	        this.m_vLeftEyebrow = iFacePart;
	        break;
	    case RIGHTEYEBROW:
	        this.m_vRightEyebrow = iFacePart;
	        break;
	    case LEFTEYE:
	        this.m_vLeftEye = iFacePart;
	        break;
	    case RIGHTEYE:
	        this.m_vRightEye = iFacePart;
	        break;
	    case NOSE:
	        this.m_vNose = iFacePart;
	        break;
	    case NOSTRIL:
	        this.m_vNostril = iFacePart;
	        break;
	    case NOSETIP:
	        this.m_vNoseTip = iFacePart;
	        break;
	    case LIPOUTERLINE:
	        this.m_vLipOuterLine = iFacePart;
	        break;
	    case LIPINNERLINE:
	        this.m_vLipInnerLine = iFacePart;
	        break;
	    case LIPUPPEROUTERLINE:
	        this.m_vLipUpperOuterLine = iFacePart;
	        break;
	    case LIPUPPERINNERLINE:
	        this.m_vLipUpperInnerLine = iFacePart;
	        break;
	    case LIPDOWNEROUTERLINE:
	        this.m_vLipLowerOuterLine = iFacePart;
	        break;
	    case LIPDOWNERINNERLINE:
	        this.m_vLipLowerInnerLine = iFacePart;
	        break;
	    case LEFTSIDEPOINTS:
	        this.m_vLeftSidePoints = iFacePart;
	        break;
	    case RIGHTSIDEPOINTS:
	        this.m_vRightSidePoints = iFacePart;
	        break;
	    case MIDLINEPOINTS:
	        this.m_vMidlinePoints = iFacePart;
	        break;
	    case EYECORNERPOINTS:
	        this.m_vEyeCornerPoints = iFacePart;
	        break;
	    case MOUTHCORNERPOINTS:
	        this.m_vMouthCornerPoints = iFacePart;
	        break;
	    case PITCHAXISLINEPOINTS:
	        this.m_vPitchAxisLinePoints = iFacePart;
	        break;
	    case LEFTIRIS:
	        this.m_vLeftIris = iFacePart;
	        break;
	    case RIGHTIRIS:
	        this.m_vRightIris = iFacePart;
	        break;
	    case LEFTEAR:
	        this.m_vLeftEar = iFacePart;
	        break;
	    case RIGHTEAR:
	        this.m_vRightEar = iFacePart;
	        break;
		case WHOLEFACE:
			break;
		case WHOLEMOUTH:
			break;
		case NONE:
			break;
	    }
	}
    public int[][] 							GetTriangleIndexes() {return this.m_vvTriangleIndexes;}
    public void								SetTriangleIndexes(final int[][] iTriangleIndexes) { this.m_vvTriangleIndexes = iTriangleIndexes;}
}
