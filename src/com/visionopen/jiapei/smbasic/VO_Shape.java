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
* Copyright (C):    2006~2021 by JIA Pei, all rights reserved.                                      *
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
* Version:          1.1                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jiapei@longervision.com                                                         *
* URL:              http://www.longervision.com                                                     *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
* Modify Date:      2021-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.smbasic;


import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

import org.eclipse.swt.graphics.ImageData;


import Jama.Matrix;
import Jama.SingularValueDecomposition;




/**
 * 
 * @author jiapei
 * @brief  VO_Shape is virtually a matrix of size M*N, 
 *         M is the number of dimension for this m_MatShape
 *         N is the number of points that consists of the m_MatShape
 * m_MatShape matrix, stored as
	 * x1 x2 x3 ... xN
	 * y1 y2 y3 ... yN
	 * ......
	 * z1 z2 z3 ... zN
 */
public class VO_Shape {
	/** Every VO_Shape is corresponding to an annotation file */
	protected String				m_sAnnotationFileName;
	 
    /** the shape model, NbOfDim * NbOfAnnotatedPoints */
	protected float[][]        		m_MatShape;

    /** whether this point in 3D coordinates is able to be seen on 2D screen - dealing with occlusion. NbOfAnnotatedPoints */
    protected boolean[]       		m_MatSeeable;
    
    protected void                 	CopyData(final VO_Shape iShape)
    {
    	if(iShape.m_sAnnotationFileName != null)
    	{
    		this.m_sAnnotationFileName = new String(iShape.m_sAnnotationFileName);
    	}
//    	this.m_sAnnotationFileName	= new String(iShape.m_sAnnotationFileName);
    	// Explained by JIA Pei. 2010-04-20
    	// clone should be prohibited some times.
    	// clone not only deep copy all data, but also the object's reference!!!
//    	this.m_MatShape = iShape.m_MatShape.clone();
    	int NbOfDim = iShape.GetNbOfDim();
    	int NbOfPoints = iShape.GetNbOfPoints();
    	this.m_MatShape = new float[NbOfDim][NbOfPoints];
    	
    	// This is wrong !
    	// java.lang.System.arraycopy(iShape.m_MatShape, 0, this.m_MatShape, 0, this.m_MatShape.length*this.m_MatShape[0].length);
    	
    	for(int i = 0; i < NbOfDim; i++)
    	{
    		// This is correct
        	java.lang.System.arraycopy(iShape.m_MatShape[i], 0, this.m_MatShape[i], 0, NbOfPoints);
//    		for(int j = 0; j < NbOfPoints; j++)
//    		{
//    			this.m_MatShape[i][j] = iShape.m_MatShape[i][j];
//    		}
    	}
    }
    
    public static final int			LEFTMOST 	= -1;
    public static final int			RIGHTMOST 	= -2;
    public static final int			TOPMOST 	= -3;
    public static final int			BOTTOMMOST 	= -4;
    public static final int			CENTER 		= -5;
    public static final int			OUTERMOST 	= -6;
    public static final int			INNERMOST 	= -7;
//    enum POINTTYPE { LEFTMOST, RIGHTMOST, TOPMOST, BOTTOMMOST, CENTER, OUTERMOST, INNERMOST};
	
	public VO_Shape() {}
	
	public VO_Shape(int NbOfDim, int NbOfPoints) 
	{
		this.m_MatShape = new float[NbOfDim][NbOfPoints];
	}

        public VO_Shape(final float[] iShape, int dim)
	{
            assert (iShape.length%dim == 0);
            int NbOfPoints = iShape.length/dim;
            this.m_MatShape = new float[dim][NbOfPoints];

            for(int i = 0; i < dim; i++)
            {
                    // This is correct
                    java.lang.System.arraycopy(iShape, NbOfPoints*i, this.m_MatShape[i], 0, NbOfPoints);
    //    		for(int j = 0; j < NbOfPoints; j++)
    //    		{
    //    			this.m_MatShape[i][j] = iShape.m_MatShape[i][j];
    //    		}
            }
        }
	
	public VO_Shape(final float[][] iShape)
	{
    	int NbOfDim = iShape.length;
    	int NbOfPoints = iShape[0].length;
    	this.m_MatShape = new float[NbOfDim][NbOfPoints];
    	
    	// This is wrong !
    	// java.lang.System.arraycopy(iShape.m_MatShape, 0, this.m_MatShape, 0, this.m_MatShape.length*this.m_MatShape[0].length);
    	
    	for(int i = 0; i < NbOfDim; i++)
    	{
    		// This is correct
        	java.lang.System.arraycopy(iShape[i], 0, this.m_MatShape[i], 0, NbOfPoints);
//    		for(int j = 0; j < NbOfPoints; j++)
//    		{
//    			this.m_MatShape[i][j] = iShape.m_MatShape[i][j];
//    		}
    	}
	}
	
	public VO_Shape(final VO_Shape iShape)
	{
		this.CopyData(iShape);
	}
	
    /** operator= overloading, similar to copy constructor */
	public VO_Shape clone (final VO_Shape s)
    {
		this.CopyData(s);
		return (this);
    }
	
	public VO_Shape add (final VO_Shape s)
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();

		VO_Shape res = new VO_Shape(this);
		for(int i = 0; i < NbOfDim; i++)
	    {
	    	for(int j = 0; j < NbOfPoints; j++)
	    	{
	    		res.m_MatShape[i][j] += s.m_MatShape[i][j];
	    	}
	    }
		
		return res;
	}
	
	public VO_Shape subtract (final VO_Shape s)
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();

		VO_Shape res = new VO_Shape(this);
		for(int i = 0; i < NbOfDim; i++)
	    {
	    	for(int j = 0; j < NbOfPoints; j++)
	    	{
	    		res.m_MatShape[i][j] -= s.m_MatShape[i][j];
	    	}
	    }
		
		return res;
	}
	
	public VO_Shape multiply (final float s)
	{
		VO_Shape res = new VO_Shape(this);
		res.Scale(s);
		
		return res;
	}
	
	public float dot(final VO_Shape iShape)
	{
		Matrix A = new Matrix( com.visionopen.jiapei.utils.Common.float2double(this.m_MatShape) );
		Matrix B = new Matrix( com.visionopen.jiapei.utils.Common.float2double(iShape.m_MatShape) );
		return (com.visionopen.jiapei.utils.Common.CalcMatrixElemSum(A.arrayTimes(B)));
	}
	
	public void Resize(int rows, int cols)
	{
		this.m_MatShape = new float[rows][cols];
	}
	
	public float[] CenterOfGravity()
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();
		
		float[] centers = new float[NbOfDim];
	    for(int i = 0; i < NbOfDim; i++)
	    {
	    	centers[i] = 0.0f;
	    	for(int j = 0; j < NbOfPoints; j++)
	    	{
	    		centers[i] += this.m_MatShape[i][j];
	    	}
	    }
    	for(int i = 0; i < NbOfDim; i++)
    		centers[i] /= NbOfPoints;
	    
 	    return centers;
	}
	
	public void Centralize()
	{
	    float[] centers = this.CenterOfGravity();
	    this.Translate(com.visionopen.jiapei.utils.Common.Neg(centers));
	}
	
	public void Translate(float[] cordShift)
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();
		
		for(int i = 0; i < NbOfDim; i++)
	    {
			for(int j = 0; j < NbOfPoints; j++)
			{
				this.m_MatShape[i][j] += cordShift[i];
			}
	    }
	}
	
	public void Scale(float ScaleSize)
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();
		
		for(int i = 0; i < NbOfDim; i++)
		{
			for(int j = 0; j < NbOfPoints; j++)
			{
				this.m_MatShape[i][j] *= ScaleSize;
			}
		}
	}
	
	public void Scale(float[] cordScale)
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();
		
		for(int i = 0; i < NbOfDim; i++)
		{
			for(int j = 0; j < NbOfPoints; j++)
			{
				this.m_MatShape[i][j] *= cordScale[i];
			}
		}
	}
	
	public void ScaleX( float sX)
	{
		int NbOfPoints = this.GetNbOfPoints();
		
		for(int j = 0; j < NbOfPoints; j++)
		{
			this.m_MatShape[0][j] *= sX;
		}
	}
	
	public void ScaleY( float sY)
	{
		int NbOfPoints = this.GetNbOfPoints();
		
		for(int j = 0; j < NbOfPoints; j++)
		{
			this.m_MatShape[1][j] *= sY;
		}
	}
	
	public void ScaleZ( float sZ)
	{
		int NbOfPoints = this.GetNbOfPoints();
		
		for(int j = 0; j < NbOfPoints; j++)
		{
			this.m_MatShape[2][j] *= sZ;
		}
	}
	
	/**
	 * Only deal with 2D and 3D rotation
	 * @param cordRot
	 */
	public void Rotate(float[] cordRot)		// angle: anticlockwise
	{
		int NbOfDim = this.GetNbOfDim();
		int NbOfPoints = this.GetNbOfPoints();

		switch(NbOfDim)
		{
		case 3:
		{
            float x, y, z;

            // Assuming the angles are in radians.
            float cph = (float)Math.cos( cordRot[0] );
            float sph = (float)Math.sin( cordRot[0] );
            float cth = (float)Math.cos( cordRot[1] );
            float sth = (float)Math.sin( cordRot[1] );
            float cps = (float)Math.cos( cordRot[2] );
            float sps = (float)Math.sin( cordRot[2] );

            // set up rotation matrix
            // http://mathworld.wolfram.com/RotationMatrix.html
            // this is equal to
            /**
             * 1            0           0
             * 0            cos(ph)     sin(ph)
             * 0            -sin(ph)    cos(ph)
             *
             * cos(th)      0           -sin(th)
             * 0            1           0
             * sin(th)      0           cos(th)
             *
             * cos(ps)      sin(ps)     0
             * -sin(ps)     cos(ps)     0
             * 0            0           1
             */
            float c00 =  cth * cps;
            float c01 =  cth * sps;
            float c02 =  -sth;
            float c10 =  sph * sth * cps - cph * sps;
            float c11 =  sph * sth * sps + cph * cps;
            float c12 =  sph * cth;
            float c20 =  cph * sth * cps + sph * sps;
            float c21 =  cph * sth * sps - sph * cps;
            float c22 =  cph * cth;

            for(int i = 0; i < NbOfPoints; i++)
            {
                x = this.m_MatShape[0][i];
                y = this.m_MatShape[1][i];
                z = this.m_MatShape[2][i];
                this.m_MatShape[0][i] =  c00*x+c01*y+c02*z;
                this.m_MatShape[1][i] =  c10*x+c11*y+c12*z;
                this.m_MatShape[2][i] =  c20*x+c21*y+c22*z;
            }
		}
			break;
		case 2:
		default:
		{
			float x, y;
		    // set up rotation matrix
		    float c00 =  (float)Math.cos( cordRot[0] );
		    float c01 = -(float)Math.sin( cordRot[0] );
		    float c10 =  (float)Math.sin( cordRot[0] );
		    float c11 =  (float)Math.cos( cordRot[0] );
	
			for(int i = 0; i < NbOfPoints; i++)
		    {
				x = this.m_MatShape[0][i];
				y = this.m_MatShape[1][i];
				
				this.m_MatShape[0][i] = c00*x + c01*y;
	        	this.m_MatShape[1][i] = c10*x + c11*y;
		    }
		}
			break;
		}
	}
	
	public void Normalize()
	{
	    this.Centralize();
	    float norm = this.GetShapeNorm();
	    this.Scale( 1.0f/norm );
	}
	
	public float GetCentralizedShapeSize()
	{
	    VO_Shape tmp = new VO_Shape();
	    tmp.clone(this);
	    tmp.Centralize();
		return tmp.GetShapeNorm();
	}
	
	public float GetShapeNorm()
	{
		return (com.visionopen.jiapei.utils.Common.CalcMatrixNorm(this.m_MatShape));
	}
	
	// get the rotation radian between this m_MatShape and the input m_MatShape
	public float[] GetRotation(final VO_Shape iRefShape)
	{
		int NbOfDim = this.GetNbOfDim();
		
		Matrix A = new Matrix(NbOfDim, NbOfDim);
		Matrix B = new Matrix(com.visionopen.jiapei.utils.Common.float2double(this.m_MatShape) );
		Matrix C = new Matrix(com.visionopen.jiapei.utils.Common.float2double(iRefShape.m_MatShape) );
		Matrix D = new Matrix(NbOfDim, NbOfDim);
		D = C.times(B.transpose());
		SingularValueDecomposition dsvd = new SingularValueDecomposition(D);
		float ddet = (float)D.det();
		
		if (ddet < 0.0)
		{
			Matrix Temp = Matrix.identity(NbOfDim, NbOfDim);
			Temp.set(NbOfDim - 1, NbOfDim - 1, -1);
			A = dsvd.getU().times(Temp).times(dsvd.getV().transpose());
		}
		else
			A = dsvd.getU().times(dsvd.getV().transpose());
		
		float[] angles;
		switch(NbOfDim)
		{
		case 3:
		{
			angles = new float[3];
			float sin_beta  = -(float)A.get(0, 2);
			float tan_alpha = (float)(A.get(1, 2) / A.get(2, 2));
			float tan_gamma = (float)(A.get(0, 1) / A.get(0, 0));

			angles[0] = (float)Math.atan(tan_alpha);
			angles[1] = (float)Math.asin(sin_beta);
			angles[2] = (float)Math.atan(tan_gamma);
		}
		break;
		case 2:
		default:
		{
			angles = new float[1];
            float cos_theta = (float)A.get(0, 0);
            float sin_theta = (float)A.get(1, 0);
            //res[0] = (float)Math.atan2(sin_theta, cos_theta);

            // cos_theta should be less than 1.0; but here, cos_theta is calculated by matrix computation,
            // in stead of by acos() function, so cos_theta might have some special values like cos_theta >= 1.0.
            if ( ( java.lang.Math.abs(1.0-cos_theta) < Double.MIN_VALUE ) || cos_theta >= 1.0 )
            {
                // cos_theta = 1  =>  shapes are already aligned
                angles[0] = 0.0f;
            }
            else if ( java.lang.Math.abs(cos_theta) < Double.MIN_VALUE )
            {
                // cos_theta = 0  =>  90 degrees rotation
                angles[0] = (float)(java.lang.Math.PI/2.0);
            }
            // cos_theta should be bigger than -1.0; but here, cos_theta is calculated by  matrix computation,
            // in stead of by acos() function, so cos_theta might have some special values like cos_theta <= -1.0.
            else if ( ( java.lang.Math.abs(1.0+cos_theta) < Double.MIN_VALUE ) || cos_theta <= -1.0 )
            {
                // cos_theta=-1  =>  180 degrees rotation
                angles[0] = (float)java.lang.Math.PI;
            }
            else
            {
                // get the rotation in radians
                float a_cos = (float)java.lang.Math.acos( cos_theta );
                float a_sin = (float)java.lang.Math.asin( sin_theta );

                // http://en.wikipedia.org/wiki/Trigonometric_function
                if (a_sin<0)
                {
                    // lower half of the unit circle
                    angles[0] = -a_cos;
                }
                else
                {
                    // upper half of the unit circle
                    angles[0] = a_cos;
                }
            }
		}
		break;
		}

		return angles;
	}
	
	public void ConstrainShapeInSize(final float[] iSize)	// iSize[0] -- width; iSize[1] -- height
	{
		int NbOfPoints = this.GetNbOfPoints();
		for(int j = 0; j < NbOfPoints; j++)
		{
			if(this.m_MatShape[0][j] < Float.MIN_VALUE)
				this.m_MatShape[0][j] = 0.0f;
			else if(this.m_MatShape[0][j] >= (iSize[0] -1.0f - Float.MIN_VALUE) )
				this.m_MatShape[0][j] = iSize[0] -1.0f - 0.1f;
				
			if(this.m_MatShape[1][j] < Float.MIN_VALUE)
				this.m_MatShape[1][j] = 0.0f;
			else if(this.m_MatShape[1][j] > (iSize[1] -1.0f - Float.MIN_VALUE) )
				this.m_MatShape[1][j] = iSize[1] -1.0f - 0.1f;
		}
	}
	
	public void ConstrainShapeInImage(final ImageData iImg)
	{
		int NbOfPoints = this.GetNbOfPoints();
		for(int j = 0; j < NbOfPoints; j++)
		{
			if(this.m_MatShape[0][j] < Float.MIN_VALUE)
				this.m_MatShape[0][j] = 0.0f;
			else if(this.m_MatShape[0][j] >= (iImg.width -1.0f - Float.MIN_VALUE) )
				this.m_MatShape[0][j] = iImg.width -1.0f - 0.1f;
				
			if(this.m_MatShape[1][j] < Float.MIN_VALUE)
				this.m_MatShape[1][j] = 0.0f;
			else if(this.m_MatShape[1][j] > (iImg.height -1.0f - Float.MIN_VALUE) )
				this.m_MatShape[1][j] = iImg.height -1.0f - 0.1f;
		}
	}
	
	// align this m_MatShape to the input m_MatShape
	public void AlignTo( final VO_Shape ref, float scale, float[] angles, float[] t)
	{
	    // make a copy of 'ref'
	    VO_Shape iShapeCpy = new VO_Shape( ref );

	    // move this and refCpy to origin
	    float[] thisCOGflt	= this.CenterOfGravity();
//		    Point2D.Float thisCOG = new Point2D.Float(thisCOGflt[0], thisCOGflt[1]);
//		    this.Translate( -(float)thisCOG.getX(), -(float)thisCOG.getY() );
	    this.Translate( com.visionopen.jiapei.utils.Common.Neg(thisCOGflt) );
	    float[] refCOGflt	= this.CenterOfGravity();
//		    Point2D.Float refCOG = new Point2D.Float(refCOGflt[0], refCOGflt[1]);
//		    iShapeCpy.Translate( -(float)refCOG.getX(), -(float)refCOG.getY() );
	    iShapeCpy.Translate( com.visionopen.jiapei.utils.Common.Neg(refCOGflt) );

	    // normalize scale, using the 2-norm
	    float thisSize = this.GetShapeNorm();
	    float refSize = iShapeCpy.GetShapeNorm();
	    this.Scale( refSize/thisSize );

	    // align rotation between this and refCpy
	    float[] theta = this.GetRotation( iShapeCpy );
	    this.Rotate( theta );

	    // translate this to ref origin
	    this.Translate( refCOGflt );
	}
	
	public void Affine2D(final Matrix affineMat)
	{
		
	}
	
    public void AlignTransformation( final VO_Shape ref, float scale, float[] angles, float[] t )
    {
    	
    }
    
    public void ProcrustesAnalysis( final VO_Shape ref, float norm, float[] angles, float[] t )
    {
    	
    }
    
    public void InverseProcrustesAnalysis( final float norm, final float[] angles, final float[] t )
    {
    	
    }
    
    public float[] Min()
	{
		int NbOfDim = this.GetNbOfDim();
	
		float[] res = new float[NbOfDim];
		res[0] = MinX();
		res[1] = MinY();
		if(NbOfDim >= 3)
			res[2] = MinZ();
		
		return res;
	}
    
	public float MinX()
	{
		int NbOfPoints = this.GetNbOfPoints();
	
		float minX = this.m_MatShape[0][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			if(this.m_MatShape[0][i] < minX)
			{
				minX = this.m_MatShape[0][i];
			}
		}
		return minX;
	}
	
	public float MinY()
	{
		int NbOfPoints = this.GetNbOfPoints();
	
		float minY = this.m_MatShape[1][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			if(this.m_MatShape[1][i] < minY)
			{
				minY = this.m_MatShape[1][i];
			}
		}
		return minY;
	}
	
	public float MinZ()
	{
		int NbOfPoints = this.GetNbOfPoints();
	
		float minZ = this.m_MatShape[2][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			if(this.m_MatShape[2][i] < minZ)
			{
				minZ = this.m_MatShape[2][i];
			}
		}
		return minZ;
	}
	
    public float[] Max()
	{
		int NbOfDim = this.GetNbOfDim();
	
		float[] res = new float[NbOfDim];
		res[0] = MaxX();
		res[1] = MaxY();
		if(NbOfDim >= 3)
			res[2] = MaxZ();
		
		return res;
	}
	
	public float MaxX()
	{
		int NbOfPoints = this.GetNbOfPoints();
	
		float maxX = this.m_MatShape[0][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			if(this.m_MatShape[0][i] > maxX)
			{
				maxX = this.m_MatShape[0][i];
			}
		}
		return maxX;
	}
	
	public float MaxY()
	{
		int NbOfPoints = this.GetNbOfPoints();
	
		float maxY = this.m_MatShape[1][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			if(this.m_MatShape[1][i] > maxY)
			{
				maxY = this.m_MatShape[1][i];
			}
		}
		return maxY;
	}
	
	public float MaxZ()
	{
		int NbOfPoints = this.GetNbOfPoints();
	
		float maxZ = this.m_MatShape[2][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			if(this.m_MatShape[2][i] > maxZ)
			{
				maxZ = this.m_MatShape[2][i];
			}
		}
		return maxZ;
	}
	
	public float[] Mean()
	{
		int NbOfDim = this.GetNbOfDim();

		float[] res = new float[NbOfDim];
		res[0] = MeanX();
		res[1] = MeanY();
		if(NbOfDim >= 3)
			res[2] = MeanZ();

		return res;
	}
	
    public float MeanX()
    {
    	int NbOfPoints = this.GetNbOfPoints();

		float oneX = this.m_MatShape[0][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			oneX += this.m_MatShape[0][i];
		}
		oneX /= NbOfPoints;
		
		return oneX;
    }
    
    public float MeanY()
    {
    	int NbOfPoints = this.GetNbOfPoints();
    	
		float oneY = this.m_MatShape[1][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			oneY += this.m_MatShape[1][i];
		}
		oneY /= NbOfPoints;
		
		return oneY;
    }
    
    public float MeanZ()
    {
    	int NbOfPoints = this.GetNbOfPoints();
    	
		float oneZ = this.m_MatShape[2][0];
		
		for(int i = 1; i < NbOfPoints; i++)
		{
			oneZ += this.m_MatShape[2][i];
		}
		oneZ /= NbOfPoints;
		
		return oneZ;
    }
	
	public Point2f GetLeftTop()
	{
		Point2f res = new Point2f();
		res.x = MinX();
		res.y = MinY(); 
		return res;
	}
	
	public Point2f GetRightBottom()
	{
		Point2f res = new Point2f();
		res.x = MaxX();
		res.y = MaxY();
		return res;
	}
	
	public Point2f GetLeftBottom()
	{
		Point2f res = new Point2f();
		res.x = MinX();
		res.y = MaxY(); 
		return res;
	}
	
	public Point2f GetRightTop()
	{
		Point2f res = new Point2f();
		res.x = MaxX();
		res.y = MinY();
		return res;
	}
	
	public Rectangle2D.Float		GetShapeRect() 
	{
									float minX = MinX();
									float minY = MinY();
									float maxX = MaxX();
									float maxY = MaxY();
									Rectangle2D.Float res = new Rectangle2D.Float();
									res.x = minX;
									res.y = minY;
									res.width = maxX - minX; 
									res.height = maxY - minY; 
									return res;
	}
	
	public Rectangle				GetShapeBoundRect()
	{
									float minX = MinX();
									float minY = MinY();
									float maxX = MaxX();
									float maxY = MaxY();
									Rectangle res = new Rectangle(0,0,0,0);
									res.x = (int) Math.floor(minX);
									res.y = (int) Math.floor(minY);
									res.width = (int) (Math.ceil(maxX)) - res.x;
									res.height = (int) (Math.ceil(maxY)) - res.y;
									return res;
	}
	
	/** Is the current point "pt" in current shape? */
	public int						IsPointInShape(final Point2f pt, final VO_Triangle2DStructure[] triangles)
	{
		VO_Triangle2DStructure[] shapeTriangles = this.GetTriangle2DStructure(triangles);
		return com.visionopen.jiapei.smbasic.VO_Triangle2DStructure.IsPointInTriangles(pt, shapeTriangles);
	}
	
	public VO_Triangle2DStructure[] GetTriangle2DStructure(VO_Triangle2DStructure[] triangles)
	{
		int NbOfTriangles  = triangles.length;
		VO_Triangle2DStructure[] shapeTriangles = new VO_Triangle2DStructure[NbOfTriangles];
		int[] vertexIdx = new int[3];;
		
		for(int i = 0; i < NbOfTriangles; ++i)
		{
			vertexIdx = triangles[i].GetVertexIndexes();
			shapeTriangles[i] = new VO_Triangle2DStructure();
			shapeTriangles[i].m_MatShape[0][0] 	= this.m_MatShape[0][vertexIdx[0]];
			shapeTriangles[i].m_MatShape[1][0] 	= this.m_MatShape[1][vertexIdx[0]];
			shapeTriangles[i].m_MatShape[0][1] 	= this.m_MatShape[0][vertexIdx[1]];
			shapeTriangles[i].m_MatShape[1][1] 	= this.m_MatShape[1][vertexIdx[1]];
			shapeTriangles[i].m_MatShape[0][2] 	= this.m_MatShape[0][vertexIdx[2]];
			shapeTriangles[i].m_MatShape[1][2] 	= this.m_MatShape[1][vertexIdx[2]];
			// explained by JIA Pei, m_dD of shapeTriangles[i] is not calculated/required here in this function
		}
		return shapeTriangles;
	}
	
    /** Get shape width */
    public float                    GetWidth() { return this.MaxX() - this.MinX(); }

    /** Get shape height */
    public float                    GetHeight() { return this.MaxY() - this.MinY(); }

    /** Get shape height */
    public float                    GetDepth() { return this.MaxZ() - this.MinZ(); }

    /** Get area (number of pixels) inside the shape */
    public int						GetArea() {return 0;}

    /** Get shape dimension */
    public int                		GetNbOfDim() { return this.m_MatShape.length; }

    /** Get Number of points */
    public int                		GetNbOfPoints() { return this.m_MatShape[0].length; }

    /** Get the shape Mat_ */
    public float[][]        		GetTheShape() { return this.m_MatShape; }
	
	/** Get a shape */
    public float					GetAShape(int idx)
	{
    								int NbOfPoints = this.m_MatShape[0].length;
									int row = idx / NbOfPoints;
									int col = idx % NbOfPoints;
									return this.m_MatShape[row][col];
	}
    public float					GetAShape(int row, int col) 
	{
									return this.m_MatShape[row][col];
	}
	
	/** Get the shape Mat_ in a row, x1x2x3...y1y2y3...z1z2z3... */
    public float[]       			GetTheShapeInARow() 
	{
									return com.visionopen.jiapei.utils.Common.reshapeTo1D(this.m_MatShape);
	}

	/** Get a single col in Mat_<float> */
    public float[]        			GetACol(int idx)
    {
							    	int NbOfDim = this.GetNbOfDim();
							    	float[] res = new float[NbOfDim];

							    	for(int i = 0; i < NbOfDim; i++)
							    	{
							    		res[i] = this.m_MatShape[i][idx];
							    	}

							    	return res;
	}
    
    /** Get 2D points */
    public Point2f[]				Get2DPoints()
    {
    	int NbOfPoints = this.m_MatShape[0].length;
    	Point2f[] res = new Point2f[NbOfPoints];
    	
    	for(int i = 0; i < NbOfPoints; i++)
    	{
    		res[i] = new Point2f();
    		res[i].x = this.m_MatShape[0][i];
    		res[i].y = this.m_MatShape[1][i];
    	}
    	
    	return res;
    }
	
	/** Get a 2D point */
    public Point2f        			GetA2DPoint(int idx)
    {
    	Point2f res = new Point2f();
    	int count = 0;
    	switch(idx)
    	{
    		case LEFTMOST:
    		{
    			int NbOfPoints = this.GetNbOfPoints();
    			float xmin = Float.MAX_VALUE;
    			for(int i = 0; i < NbOfPoints; i++)
    			{
    				if(this.m_MatShape[0][i] < xmin)
    				{
    					xmin = this.m_MatShape[0][i];
    					count = i;
    				}
    			}
    		}
    		break;
    		case RIGHTMOST:
    		{
    			int NbOfPoints = this.GetNbOfPoints();
    			float xmax = -Float.MAX_VALUE;
    			for(int i = 0; i < NbOfPoints; i++)
    			{
    				if(this.m_MatShape[0][i] > xmax)
    				{
    					xmax = this.m_MatShape[0][i];
    					count = i;
    				}
    			}
    		}
    		break;
    		case TOPMOST:
    		{
    			int NbOfPoints = this.GetNbOfPoints();
    			float ymin = Float.MAX_VALUE;
    			for(int i = 0; i < NbOfPoints; i++)
    			{
    				if(this.m_MatShape[1][i] < ymin)
    				{
    					ymin = this.m_MatShape[1][i];
    					count = i;
    				}
    			}
    		}
    		break;
    		case BOTTOMMOST:
    		{
    			int NbOfPoints = this.GetNbOfPoints();
    			float ymax = -Float.MAX_VALUE;
    			for(int i = 0; i < NbOfPoints; i++)
    			{
    				if(this.m_MatShape[1][i] > ymax)
    				{
    					ymax = this.m_MatShape[1][i];
    					count = i;
    				}
    			}
    		}
    		break;
    		case CENTER:
    		{
    			float[] mean = this.Mean();
    			res.x = mean[0];
    			res.y = mean[1];
    			return res;
    		}
//    		break;
    		default:
    		{
    			count = idx;
    		}
    		break;
    	}
    	
    	res.x = this.m_MatShape[0][count];
    	res.y = this.m_MatShape[1][count];
    	return res;
    }
    
    /** Get 3D points */
    public Point3f[]				Get3DPoints()
    {
    	int NbOfPoints = this.m_MatShape[0].length;
    	Point3f[] res = new Point3f[NbOfPoints];
    	
    	for(int i = 0; i < NbOfPoints; i++)
    	{
    		res[i] = new Point3f();
    		res[i].x = this.m_MatShape[0][i];
    		res[i].y = this.m_MatShape[1][i];
    		res[i].z = this.m_MatShape[2][i];
    	}
    	
    	return res;
    }
	
	/** Get a 3D point */
    public Point3f        			GetA3DPoint(int idx)
	{
		Point3f res = new Point3f();
		int count = 0;
		switch(idx)
		{
			case LEFTMOST:
			{
				int NbOfPoints = this.GetNbOfPoints();
				float xmin = Float.MAX_VALUE;
				for(int i = 0; i < NbOfPoints; i++)
				{
					if(this.m_MatShape[0][i] < xmin)
					{
						xmin = this.m_MatShape[0][i];
						count = i;
					}
				}
			}
			break;
			case RIGHTMOST:
			{
				int NbOfPoints = this.GetNbOfPoints();
				float xmax = -Float.MAX_VALUE;
				for(int i = 0; i < NbOfPoints; i++)
				{
					if(this.m_MatShape[0][i] > xmax)
					{
						xmax = this.m_MatShape[0][i];
						count = i;
					}
				}
			}
			break;
			case TOPMOST:
			{
				int NbOfPoints = this.GetNbOfPoints();
				float ymin = Float.MAX_VALUE;
				for(int i = 0; i < NbOfPoints; i++)
				{
					if(this.m_MatShape[1][i] < ymin)
					{
						ymin = this.m_MatShape[1][i];
						count = i;
					}
				}
			}
			break;
			case BOTTOMMOST:
			{
				int NbOfPoints = this.GetNbOfPoints();
				float ymax = -Float.MAX_VALUE;
				for(int i = 0; i < NbOfPoints; i++)
				{
					if(this.m_MatShape[1][i] > ymax)
					{
						ymax = this.m_MatShape[1][i];
						count = i;
					}
				}
			}
			break;
			case INNERMOST:
			{
				int NbOfPoints = this.GetNbOfPoints();
				float zmin = Float.MAX_VALUE;
				for(int i = 0; i < NbOfPoints; i++)
				{
					if(this.m_MatShape[2][i] < zmin)
					{
						zmin = this.m_MatShape[2][i];
						count = i;
					}
				}
			}
			break;
			case OUTERMOST:
			{
				int NbOfPoints = this.GetNbOfPoints();
				float zmax = -Float.MAX_VALUE;
				for(int i = 0; i < NbOfPoints; i++)
				{
					if(this.m_MatShape[2][i] > zmax)
					{
						zmax = this.m_MatShape[2][i];
						count = i;
					}
				}
			}
			break;
			case CENTER:
			{
				float[] mean = this.Mean();
				res.x = mean[0];
				res.y = mean[1];
				res.z = mean[2];
				return res;
			}
//			break;
			default:
			{
				count = idx;
			}
			break;
		}
		
		res.x = this.m_MatShape[0][count];
		res.y = this.m_MatShape[1][count];
		res.z = this.m_MatShape[2][count];
		return res;
	}
	
	/** Seeable point list */
    public boolean[]       			GetSeeable() { return this.m_MatSeeable; }
    
    /** Get a sub shape from shape */
    public VO_Shape					GetSubShape(final int[] iPtIdx)
	{
									int NbOfDim = this.m_MatShape.length;
									int NbOfSub = iPtIdx.length;
									float[][] oShape = new float[NbOfDim][NbOfSub];
									for(int i = 0; i < NbOfSub; i++)
									{
										for(int j = 0; j < NbOfDim; j++)
											oShape[j][i] = this.m_MatShape[j][iPtIdx[i]];
									}
									return new VO_Shape(oShape);
	}
    
    public static VO_Shape			Combine2Shapes(final VO_Shape shape1, final VO_Shape shape2)
    {
    	int NbOfPoints1 = shape1.GetNbOfPoints();
    	int NbOfPoints2 = shape2.GetNbOfPoints();
    	int NbOfDim1 = shape1.GetNbOfDim();
    	int NbOfDim2 = shape2.GetNbOfDim();
    	if( (NbOfPoints1==0 && NbOfPoints2 == 0) || (NbOfDim1==0 && NbOfDim2==0) )
    		return null;

    	if(NbOfPoints1!=0 && NbOfPoints2 != 0)
    	{
    		if(NbOfDim1 != NbOfDim2)
    		{
    			System.out.println("VO_Shape " + "The 2 shapes must have the same dim for combination");
    			System.exit(1);
    		}
    	}

    	int NbOfDim = NbOfDim1==0? NbOfDim2:NbOfDim1;
    	int NbOfPoints = NbOfPoints1 + NbOfPoints2;
    	float[][] res = new float[NbOfDim][NbOfPoints];

    	for(int i = 0; i < NbOfDim; i++)
    	{
    		for(int j = 0; j < NbOfPoints1; j++)
    		{
    			res[i][j] = shape1.m_MatShape[i][j];
    		}
    		
    		for(int j = 0; j < NbOfPoints2; j++)
    		{
    			res[i][j+shape1.GetNbOfPoints()] = shape2.m_MatShape[i][j];
    		}
    	}

    	return new VO_Shape(res);
    }
    
	/** Set a shape */
    public void        				SetAShape(int row, int col, float value)
	{
									this.m_MatShape[row][col] = value;
	}

	/** Set a 2D point */
    public void        				SetA2DPoint(final Point2f pt, int idx)
	{
									assert (this.m_MatShape.length == 2);
									this.m_MatShape[0][idx] = pt.x;
									this.m_MatShape[1][idx] = pt.y;
	}

	/** Set a 3D point */
    public void        				SetA3DPoint(final Point3f pt, int idx)
	{
									assert (this.m_MatShape.length == 3);
									this.m_MatShape[0][idx] = pt.x;
									this.m_MatShape[1][idx] = pt.y;
									this.m_MatShape[2][idx] = pt.z;
	}

    /** Set The shape */
    public void                     SetTheShape(final float[][] iShape)
    {
									this.m_MatShape = iShape;
    }

	/** Set The shape in ND. iShape is of size 1*cols */
    public void                     SetTheShape(final float[] iShape, int dim)
	{
									assert ( iShape.length%dim == 0);
									this.m_MatShape = com.visionopen.jiapei.utils.Common.reshapeTo2D(iShape, dim);
	}

    /** Set seeable */
    public void                    	SetSeeable(final boolean[] iSeeable) { this.m_MatSeeable = iSeeable; }
	
	/** Set Annotation file name */
    public void 					SetAnnotationFileName(final String fn) { this.m_sAnnotationFileName = fn; }

    /** Combine multiple 2D shapes into an entire one */
	public static float[][] 		CombineShapes2Matrix(VO_Shape[] iShapes)
	{
									int NbOfSamples = iShapes.length;
									int NbOfDim = iShapes[0].GetNbOfDim();
									int NbOfPoints = iShapes[0].GetNbOfPoints() / NbOfDim;
									float[][] res = new float[NbOfSamples*NbOfDim][NbOfPoints];
									
									for(int i = 0; i < NbOfSamples; ++i)
									{
										java.lang.System.arraycopy(iShapes, i, res, i*NbOfDim, NbOfDim*NbOfPoints);
									}
									
									return res;
	}
	
	public void print()
	{
		for(int i = 0; i < this.m_MatShape.length; i++)
			System.out.print(this.m_MatShape[i] + " ");
		System.out.println();
	}

}
