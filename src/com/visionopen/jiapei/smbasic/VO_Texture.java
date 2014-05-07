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


import Jama.Matrix;




/**
 * 
 * @author jiapei
 * @brief  VO_Texture is virtually a matrix of size M*N, 
 *         M is the number of dimension for this m_MatTexture
 *         N is the number of points that consists of the m_MatTexture
 * m_MatTexture matrix, stored as
	 * b1 b2 b3 ... bN
	 * g1 g2 g3 ... gN
	 * r1 r2 r3 ... rN
 */
public class VO_Texture {
	/** Every VO_Texture is corresponding to an image file */
	protected String				m_sImageFileName;

    /** Generalized texture representation, the texture model. NbOfTextureRepresentation * NbOfPixels */
	protected float[][]        		m_MatTexture;
	
    protected void                  CopyData( final VO_Texture iTexture )
    {
    	int NbOfTextureRepresentation = iTexture.GetNbOfTextureRepresentation();
    	int NbOfPixels = iTexture.GetNbOfPixels();
    	this.m_MatTexture = new float[NbOfTextureRepresentation][NbOfPixels];
    	
    	// This is wrong !
    	// java.lang.System.arraycopy(iTexture.m_MatTexture, 0, this.m_MatTexture, 0, this.m_MatTexture.length*this.m_MatTexture[0].length);
    	
    	for(int i = 0; i < NbOfTextureRepresentation; i++)
    	{
    		// This is correct
        	java.lang.System.arraycopy(iTexture.m_MatTexture[i], 0, this.m_MatTexture[i], 0, NbOfPixels);
//    		for(int j = 0; j < NbOfPoints; j++)
//    		{
//    			this.m_MatShape[i][j] = iShape.m_MatShape[i][j];
//    		}
    	}
    }
	
	public VO_Texture() {}
	
	public VO_Texture(int NbOfTextureRepresentation, int NbOfPixels)
	{
		this.m_MatTexture = new float[NbOfTextureRepresentation][NbOfPixels];
	}
	
	public VO_Texture(final float[][] iTexture)
	{
    	int NbOfTextureRepresentation = iTexture.length;
    	int NbOfPixels = iTexture[0].length;
    	this.m_MatTexture = new float[NbOfTextureRepresentation][NbOfPixels];
    	
    	// This is wrong !
    	// java.lang.System.arraycopy(iTexture.m_MatTexture, 0, this.m_MatTexture, 0, this.m_MatTexture.length*this.m_MatTexture[0].length);
    	
    	for(int i = 0; i < NbOfTextureRepresentation; i++)
    	{
    		// This is correct
        	java.lang.System.arraycopy(iTexture[i], 0, this.m_MatTexture[i], 0, NbOfPixels);
//    		for(int j = 0; j < NbOfPixels; j++)
//    		{
//    			m_MatTexture[i][j] = iTexture.m_MatTexture[i][j];
//    		}
    	}
	}
	
	public VO_Texture(final VO_Texture iTexture)
	{
		this.CopyData(iTexture);
	}
	
    /** operator= overloading, similar to copy constructor */
	public VO_Texture clone (final VO_Texture t)
    {
		this.CopyData(t);
		return (this);
    }
	
	public VO_Texture add (final VO_Texture t)
	{
		int NbOfTextureRepresentation = this.GetNbOfTextureRepresentation();
		int NbOfPixels = this.GetNbOfPixels();

		VO_Texture res = new VO_Texture(this);
		for(int i = 0; i < NbOfTextureRepresentation; i++)
	    {
	    	for(int j = 0; j < NbOfPixels; j++)
	    	{
	    		res.m_MatTexture[i][j] += t.m_MatTexture[i][j];
	    	}
	    }
		
		return res;
	}
	
	public VO_Texture subtract (final VO_Texture t)
	{
		int NbOfTextureRepresentation = this.GetNbOfTextureRepresentation();
		int NbOfPixels = this.GetNbOfPixels();

		VO_Texture res = new VO_Texture(this);
		for(int i = 0; i < NbOfTextureRepresentation; i++)
	    {
	    	for(int j = 0; j < NbOfPixels; j++)
	    	{
	    		res.m_MatTexture[i][j] -= t.m_MatTexture[i][j];
	    	}
	    }
		
		return res;
	}
	
	public VO_Texture multiply (final float s)
	{
		VO_Texture res = new VO_Texture(this);
		res.Scale(s);
		
		return res;
	}
	
	public float dot(final VO_Texture iTexture)
	{
		Matrix A = new Matrix( com.visionopen.jiapei.utils.Common.float2double(this.m_MatTexture) );
		Matrix B = new Matrix( com.visionopen.jiapei.utils.Common.float2double(iTexture.m_MatTexture) );
		return (com.visionopen.jiapei.utils.Common.CalcMatrixElemSum(A.arrayTimes(B)));
	}
	
	public void 					Resize(int rows, int cols)
	{
		this.m_MatTexture = new float[rows][cols];
	}
	
	public void                     Centralize()
	{
		float[] avg = this.Mean();
		this.Shift(com.visionopen.jiapei.utils.Common.Neg(avg));
	}
	
	public void                     Scale( float s)
	{
		int NbOfTextureRepresentation = this.m_MatTexture.length;
		int NbOfPixels = this.m_MatTexture[0].length;
		
		for(int i = 0; i < NbOfTextureRepresentation; i++)
		{
			for(int j = 0; j < NbOfPixels; j++)
			{
				this.m_MatTexture[i][j] *= s;
			}
		}
	}
	
	public void 					Shift( float s)
	{
		int NbOfTextureRepresentation = this.m_MatTexture.length;
		int NbOfPixels = this.m_MatTexture[0].length;
		
		for(int i = 0; i < NbOfTextureRepresentation; i++)
		{
			for(int j = 0; j < NbOfPixels; j++)
			{
				this.m_MatTexture[i][j] += s;
			}
		}
	}
	
	public void                     Normalize()
	{
		this.Centralize();
		
		float norm = this.GetTextureNorm();

	    this.Scale( 1.0f/norm );
	}
	
	public float                    GetStandardizedTextureNorm()
	{
		VO_Texture tmp = new VO_Texture();
	    tmp.clone(this);
	    tmp.Centralize();		
		return tmp.GetTextureNorm();
	}
	
	public float                    GetTextureNorm()
	{
		return (com.visionopen.jiapei.utils.Common.CalcMatrixNorm(this.m_MatTexture));
	}

	/** Clamp the texture, constrain between infimum and supremum */
	public void						Clamp(float inf, float sup)
	{
		int NbOfTextureRepresentation = this.m_MatTexture.length;
		int NbOfPixels = this.m_MatTexture[0].length;
		
		for(int i = 0; i < NbOfTextureRepresentation; i++)
		{
			for(int j = 0; j < NbOfPixels; j++)
			{
				if (this.m_MatTexture[i][j] > sup) this.m_MatTexture[i][j] = sup;
				if (this.m_MatTexture[i][j] < inf) this.m_MatTexture[i][j] = inf;
			}
		}
	}

	public int 						GetNbOfTextureRepresentation() { return this.m_MatTexture.length; }
	
	public int 						GetNbOfPixels() { return this.m_MatTexture[0].length; }

	public float[][] 				GetTheTexture() { return this.m_MatTexture; }
	
	/** Get a texture */
	public float					GetATexture(int idx) 
	{
									int NbOfPixels = this.m_MatTexture[0].length;
									int row = idx / NbOfPixels;
									int col = idx % NbOfPixels;
									return this.m_MatTexture[row][col];
	}
	public float					GetATexture(int row, int col) 
	{
									return this.m_MatTexture[row][col];
	}

	/** Get the shape Mat_ in a row, x1x2x3...y1y2y3...z1z2z3... */
    public float[]       			GetTheTextureInARow() 
	{
									return com.visionopen.jiapei.utils.Common.reshapeTo1D(this.m_MatTexture);
	}

	/** Get a single col in Mat_<float> */
    public float[]        			GetACol(int idx)
    {
							    	int NbOfTextureRepresentation = this.GetNbOfTextureRepresentation();
							    	float[] res = new float[NbOfTextureRepresentation];

							    	for(int i = 0; i < NbOfTextureRepresentation; i++)
							    	{
							    		res[i] = this.m_MatTexture[i][idx];
							    	}

							    	return res;
	}
	
	public float[] 					GetAPixel(int idx)
	{
									int NbOfTextureRepresentation = this.m_MatTexture.length;
									float[] res = new float[NbOfTextureRepresentation];
									for (int i = 0; i < NbOfTextureRepresentation; i++)
									{
										res[i] = this.m_MatTexture[i][idx];
									}
									return res;
	}
	
	public float[] 					GetPixels(int idx)
	{
									int NbOfTextureRepresentation = this.m_MatTexture.length;
									float[] res = new float[NbOfTextureRepresentation];
									for (int i = 0; i < NbOfTextureRepresentation; i++)
									{
										res[i] = this.m_MatTexture[i][idx];
									}
									return res;
	}
	
    public void        				SetATexture(int row, int col, float value)
	{
									this.m_MatTexture[row][col] = value;
	}
    
    /** Set the texture. Not a hardcopy, but copy the header and data position */
    public void                     SetTheTexture(final float[][] iTexture) { this.m_MatTexture = iTexture;}

	/** Set The texture in ND. iTexture is of size 1*cols. Not a hardcopy, but copy the header and data position */
    public void                     SetTheTexture(final float[] iTexture, int textureRep)
	{
									assert (iTexture.length%textureRep == 0);
									this.m_MatTexture = com.visionopen.jiapei.utils.Common.reshapeTo2D(iTexture, textureRep);
	}	
    
	public void 					SetAPixel(float[] pt, int idx)
	{
									int NbOfTextureRepresentation = this.m_MatTexture.length;
									assert(pt.length == NbOfTextureRepresentation);
									for (int i = 0; i < NbOfTextureRepresentation; i++)
									{
										this.m_MatTexture[i][idx] = pt[i];
									}
	}
	
	
	public void Shift(float[] shift)
	{
		int NbOfTextureRepresentation = this.m_MatTexture.length;
		int NbOfPixels = this.m_MatTexture[0].length;
		
		for(int i = 0; i < NbOfTextureRepresentation; i++)
		{
			for(int j = 0; j < NbOfPixels; j++)
			{
				this.m_MatTexture[i][j] += shift[i];
			}
		}
	}
	
	public float[] Mean()
	{
		int NbOfTextureRepresentation = this.m_MatTexture.length;
		int NbOfPixels = this.m_MatTexture[0].length;
		
		float[] meanTextureRepresentation = new float[NbOfTextureRepresentation];
	    for(int i = 0; i < NbOfTextureRepresentation; i++)
	    {
	    	meanTextureRepresentation[i] = 0.0f;
	    	for(int j = 0; j < NbOfPixels; j++)
	    	{
	    		meanTextureRepresentation[i] += this.m_MatTexture[i][j];
	    	}
	    }
    	for(int i = 0; i < NbOfTextureRepresentation; i++)
    		meanTextureRepresentation[i] /= NbOfPixels;
	    
 	    return meanTextureRepresentation;
	}
	
	public float CalcTextureMeanAll()
	{
		int NbOfTextureRepresentation = this.m_MatTexture.length;
		float[] meanTextureRepresentation = this.Mean();
		float meanAll = 0.0f;
		for(int i = 0; i < NbOfTextureRepresentation; i++)
		{
			meanAll += meanTextureRepresentation[i];
		}
		meanAll /= NbOfTextureRepresentation;
	    
 	    return meanAll;
	}
	
//	public float GetStandardDeviation()
//	{
//		int NbOfTextureRepresentation = this.m_MatTexture.length;
//		int NbOfPixels = this.m_MatTexture[0].length;
//		
//		float mean = this.CalcTextureMeanAll();
//		float sd = 0.0f;
//		for (int i = 0; i < NbOfTextureRepresentation; i++)
//		{
//			for (int j = 0; j < NbOfPixels; j++)
//			{
//				sd += Math.pow( (this.m_MatTexture[i][j]-mean), 2);
//			}
//		}
//		
//		sd = (float)Math.sqrt(sd/(NbOfTextureRepresentation*NbOfPixels));
//		
//		return sd;
//	}
	
	// Several methods could be used to implement standardization
	// 1) zero mean unit length -- refer to Stegmann "aam_api" 
	// 2) normal distribution normalization -- used here
	// 3) average normalization
//	public float Standardize()
//	{
//		int NbOfTextureRepresentation = this.m_MatTexture.length;
//		int NbOfPixels = this.m_MatTexture[0].length;
//		
//		float mean = this.CalcTextureMeanAll();
//		float norm = this.GetTextureNorm();			// norm: m_MatTexture 2-norm
//		
//		// deal with the member variables "m_MatTexture"
//		if (norm <= Double.MIN_VALUE )
//		{
//			for (int i = 0; i < NbOfTextureRepresentation; i++)
//			{
//				for (int j = 0; j < NbOfPixels; j++)
//				{
//					this.m_MatTexture[i][j] = 0.0f;
//				}
//			}
//		}
//		else
//		{
//			float temp = 1.0f/norm;
//			for (int i = 0; i < NbOfTextureRepresentation; i++)
//			{
//				for (int j = 0; j < NbOfPixels; j++)
//				{
//					this.m_MatTexture[i][j] = (this.m_MatTexture[i][j] - mean) * temp;
//				}
//			}
//		}
//		
//		return norm;
//	}
}

