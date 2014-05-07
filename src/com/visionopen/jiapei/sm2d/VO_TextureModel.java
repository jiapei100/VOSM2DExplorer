/********************************************************************************
*                                                                           	*
*   IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.       	*
*                                                                           	*
*   By downloading, copying, installing or using the software you agree to this *
*   license. If you do not agree to this license, do not download, install,    	*
*   copy or use the software.                                      				*
*                                                                           	*
*                           	License Agreement                              	*
*          			For Vision Open Statistical Model 2D Explorer               *
*                                                                           	*
*   Copyright (C):      2010~2012 by JIA Pei, all rights reserved.          	*
*                                                                           	*
*   VOSM2DExplorer is free software under the terms of the GNU Lesser General	*
*   Public License (GNU LGPL) as published by the Free Software Foundation; 	*
*   either version 3.0 of the License, or (at your option) any later version.  	*
*   You can use it, modify it, redistribute it, etc; and redistribution and use	*
*   in source and binary forms, with or without modification, are permitted    	*
*   provided that the following conditions are met:								*
*                                                                           	*
*   a) Redistribution's of source code must retain this whole paragraph of  	*
*   copyright notice, including this list of conditions and all the following   *
*   contents in this  copyright paragraph.                        				*
*                                                                           	*
*   b) Redistribution's in binary form must reproduce this whole paragraph of 	*
*   copyright notice, including this list of conditions and all the following 	*
*   contents in this copyright paragraph, and/or other materials provided with 	*
*   the distribution.                                         					*
*                                                                           	*
*   c) The name of the copyright holders may not be used to endorse or promote 	*
*   products derived from this software without specific prior written      	*
*   permission.                                                     			*
*                                                                           	*
*   Any publications based on this code must cite the following 2 papers,		*
*   technical reports and on-line materials.                                	*
*   1) P. JIA, 2D Statistical Models, Technical Report of Vision Open Working  	*
*   Group, 2st Edition, October 21, 2010.                           			*
*   http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf*
*   2) P. JIA. Audio-visual based HMI for an Intelligent Wheelchair.        	*
*   PhD thesis, University of Essex, February, 2011.                        	*
*   http://www.visionopen.com/members/jiapei/publications/pei_phdthesis2010.pdf	*
*                                                                           	*
* Version:          1.0                                                     	*
* Author:           JIA Pei                                                 	*
* Contact:          jp4work@gmail.com                                       	*
* URL:              http://www.visionopen.com                               	*
* Modified Date:    2012-05-22                                              	*
********************************************************************************/
package com.visionopen.jiapei.sm2d;

import java.io.*;

import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.Vector;

import javax.vecmath.Point2f;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;

import Jama.Matrix;

import com.visionopen.jiapei.smbasic.VO_Edge;
import com.visionopen.jiapei.smbasic.VO_Shape;
import com.visionopen.jiapei.smbasic.VO_Texture;
import com.visionopen.jiapei.smbasic.VO_Triangle2DStructure;
import com.visionopen.jiapei.smbasic.VO_WarpingPoint;
import com.visionopen.jiapei.utils.PCA;




public class VO_TextureModel extends VO_ShapeModel 
{
	/** PCA transform for texture, including eigenvectors, eigenvalues, and mean */
	public PCA							m_PCANormalizedTexture;

    /** Texture representation method - DIRECTTEXTURE, LAPLACETEXTURE, HARRISCORNERTEXTURE, HISTOGRAMEQUALIZED, GABOR, SEGMENTATION, etc. */
	protected int                		m_iTextureRepresentationMethod;

    /** Number of texture representations could be more or less than m_iNbOfChannels */
	protected int                		m_iNbOfTextureRepresentations;

	/** COLOR or Gray-level - 3 - COLOR; 1 - Gray-level */
	protected int						m_iNbOfChannels;

    /** Number of pixels in template face convex hull or concave hull. For IMM, 30132 */
	protected int                		m_iNbOfPixels;

    /** length of texture vector in format of b1b2b3...g1g2g3...r1r2r3.... m_iNbOfTextureRepresentation*m_iNbOfPixels. For IMM, 30132*3=90396 */
	protected int                		m_iNbOfTextures;

    /** Most possible texture eigens before PCA. For IMM: min (90396, 240) = 240 */
	protected int                		m_iNbOfEigenTexturesAtMost;

    /** Number of texture model eigens. For IMM: 127 */
	protected int               		m_iNbOfTextureEigens;

    /** Reference texture which is of size close to "1", but not guaranteed */
	protected VO_Texture         		m_VONormalizedMeanTexture;

    /** Reference texture which is scaled back to the original gray/color intensities */
	protected VO_Texture            	m_VOReferenceTexture;

    /** The template texture average standard deviation : 12364.1 */
	protected float                 	m_fAverageTextureStandardDeviation;

    /** Truncate Percentage for texture model PCA. Normally, 0.95 */
	protected float                 	m_fTruncatedPercent_Texture;

    /** All loaded textures in a vector format. For IMM, 240*90396 */
	protected VO_Texture[]         		m_vTextures;

    /** All normalized textures in a vector format. For IMM, 240*90396 */
	protected VO_Texture[]         		m_vNormalizedTextures;
    
    /** Template face image */
	protected ImageData         		m_ImageTemplateFace;

    /** Image of edges */
	protected ImageData         		m_ImageEdges;
	
	/** Image of 2D normal distribution elllipses */
	protected ImageData					m_ImageEllipses;

	/** Unnormalized point warping information. For IMM, 30132 */
	protected VO_WarpingPoint[]			m_vTemplatePointWarpInfo;

    /** Normalized point warping information. For IMM, 30132 */
	protected VO_WarpingPoint[]  		m_vNormalizedPointWarpInfo;

	/** We need these image file names for later image loading */
	protected String[]					m_vStringTrainingImageNames;

    /** Initialization */
	protected void init() {
		super.init();
    	this.m_iTextureRepresentationMethod		= 1;
    	this.m_iNbOfTextureRepresentations		= 0;
    	this.m_iNbOfChannels					= 3;
    	this.m_iNbOfPixels						= 0;
    	this.m_iNbOfTextures					= 0;
    	this.m_iNbOfEigenTexturesAtMost			= 0;
    	this.m_iNbOfTextureEigens				= 0;
    	this.m_fAverageTextureStandardDeviation	= 0.0f;
    	this.m_fTruncatedPercent_Texture		= 0.95f;
    	
	    this.m_PCANormalizedTexture				= null;
		this.m_VONormalizedMeanTexture			= null;
		this.m_VOReferenceTexture				= null;
		this.m_vTextures						= null;
		this.m_vNormalizedTextures				= null;
		this.m_ImageTemplateFace				= null;
		this.m_ImageEdges						= null;
		this.m_vTemplatePointWarpInfo			= null;
		this.m_vNormalizedPointWarpInfo			= null;
		this.m_vStringTrainingImageNames		= null;
    }
    
	public VO_TextureModel() {
		init();
	}
	
    /**Calculate point warping information */
	public static int         			VO_CalcPointWarpingInfo(final VO_Triangle2DStructure[] templateTriangles, VO_WarpingPoint[] warpInfo)
    {
		Vector<VO_WarpingPoint>			vec = null;
	    int NbOfPixels     = 0;

	    Rectangle rect               	= VO_TextureModel.VO_CalcBoundingRectFromTriangles(templateTriangles);

	    for (int i = 0; i < rect.height; i++)
	    {
	        for (int j = 0; j < rect.width; j++)
	        {
	            // JIA Pei. 2006-11-25. You will see the following (int) is very important
	            // without (int), the result here is not correct at all!!
	            Point2f pt = new Point2f((float)j, (float)i);
				
				int k = VO_Triangle2DStructure.IsPointInTriangles(pt,templateTriangles);
				if(k>=0)
				{
					VO_WarpingPoint tempPixelTriangle = new VO_WarpingPoint();

					// necessary for all methods
					tempPixelTriangle.SetPosition(pt);
					tempPixelTriangle.SetTriangleIndex(k);
					tempPixelTriangle.SetPointIndex(NbOfPixels);
					tempPixelTriangle.SetTriangle2DStructure(templateTriangles[k ] );

					// Very important!! Note by JIA Pei, push_back cannot perform on 2-D vectors
					vec.add (tempPixelTriangle);
//					warpInfo.push_back (tempPixelTriangle);

					NbOfPixels ++;
	            }
	        }
	    }
	    warpInfo = (VO_WarpingPoint[]) vec.toArray();
	    return NbOfPixels;
    }

    /** Load a texture vector from the image "img", in terms of "iShape", with a texture building method */
	public static boolean              	VO_LoadOneTextureFromShape(final VO_Shape iShape, 
																	final ImageData img, 
																	final VO_Triangle2DStructure[] templateTriangles, 
																	final VO_WarpingPoint[] warpInfo, 
																	VO_Texture oTexture, 
																	int trm)
    {
    	return false;
    }

    /** Normalize all textures */
	public static float                	VO_NormalizeAllTextures(final VO_Texture[] vTextures, VO_Texture[] normalizedTextures)
    {
	    int NbOfSamples                    		= vTextures.length;
	    int NbOfPixels                   		= vTextures[0].GetNbOfPixels();
	    float averageTextureStandardDeviation	= 0.0f;

	    normalizedTextures = vTextures;

	    for(int i = 0; i < NbOfSamples; i++)
	    {
	        averageTextureStandardDeviation += normalizedTextures[i].GetStandardizedTextureNorm();
			normalizedTextures[i].Normalize();
	    }
	    averageTextureStandardDeviation /= (float)NbOfSamples;

	    return averageTextureStandardDeviation;
    }

    /** Rescale all normalized textures */
	public static void                 	VO_RescaleAllNormalizedTextures(final VO_Texture meanNormalizeTexture, VO_Texture[] normalizedTextures)
    {
	    int NbOfSamples = normalizedTextures.length;

	    // Explained by JIA Pei. 2010-02-10. scale back so that the mean shape size is equal to 1.0
	    for(int i = 0; i < NbOfSamples; i++)
	    {
	        VO_TextureModel.VO_RescaleOneNormalizedTexture( meanNormalizeTexture, normalizedTextures[i] );
	    }
    }

    /** Rescale one normalized texture */
	public static void                 	VO_RescaleOneNormalizedTexture(final VO_Texture meanNormalizeTexture, VO_Texture normalizedTexture)
    {
	    float ts = (float) Math.sqrt ( Math.abs(normalizedTexture.dot(meanNormalizeTexture) ) );
	    normalizedTexture.Scale( 1.0f/ts );
    }

    /** Returns the mean texture of all textures */
	public static void                 	VO_CalcMeanTexture(final VO_Texture[] vTextures, VO_Texture meanTexture)
    {
	    int NbOfSamples    = vTextures.length;
	    meanTexture = vTextures[0];

	    for(int i = 1; i < NbOfSamples; i++)
	    {
	        meanTexture = meanTexture.add(vTextures[i]);
	    }

	    meanTexture.Scale(1.0f/(float)NbOfSamples);
    }

    /** Put one texture to the template face. Just for display! */
	public static void                 	VO_PutOneTextureToTemplateShape(final VO_Texture texture, final VO_Triangle2DStructure[] triangles, ImageData oImg)
    {
		int NbOfChannels	= texture.GetNbOfTextureRepresentation() >= 3 ? 3:1;
		VO_Shape shape 		= VO_Triangle2DStructure.Triangle2D2Shape(triangles);
		Rectangle rect 		= shape.GetShapeBoundRect();
		oImg 				= new ImageData(rect.width, rect.height, 8, VO_2DSMExplorer.palette);

		Point2f pt = new Point2f();
	    // Get the image m_ImageTemplateFace, for alignment!
	    int PixelsInFaceTemplate = 0;
	    
		if(NbOfChannels == 1)
		{
			for (int Y = 0; Y < oImg.height; Y++)
			{
				for (int X = 0; X < oImg.width; X++)
				{
					pt.x = (float)X;
					pt.y = (float)Y;
	
					int k = VO_Triangle2DStructure.IsPointInTriangles(pt, triangles);
					if(k>=0)
					{
						int thePixel = (int)texture.GetATexture(0, PixelsInFaceTemplate);
						oImg.setPixel(X, Y, thePixel);
						PixelsInFaceTemplate++;
					}
				}
			}
		}
		else if(NbOfChannels == 3)
		{
			for (int Y = 0; Y < oImg.height; Y++)
			{
				for (int X = 0; X < oImg.width; X++)
				{
					pt.x = (float)X;
					pt.y = (float)Y;
	
					int k = VO_Triangle2DStructure.IsPointInTriangles(pt, triangles);
					if(k>=0)
					{
						int thePixel = ((int)texture.GetATexture(0, PixelsInFaceTemplate) & 0xff)<<16
										+((int)texture.GetATexture(1, PixelsInFaceTemplate) & 0xff)<<8
										+((int)texture.GetATexture(2, PixelsInFaceTemplate) & 0xff);
						oImg.setPixel(X, Y, thePixel);
						PixelsInFaceTemplate++;
					}
				}
			}
		}
    }

    /** Warp form one shape to another */
	public static ImageData                	VO_WarpFromOneShapeToAnother(final VO_Shape iShape, final VO_Shape oShape, final VO_Triangle2DStructure[] triangles, final ImageData iImg)
    {
		int NbOfChannels	= iImg.bytesPerLine/iImg.width;
	    int NbOfTriangles  	= triangles.length;
	    int NbOfPixels     	= 0;

		double[][] src 		= new double[3][3];
		double[][] dst 		= new double[2][3];
		Matrix[] matWarping = new Matrix[NbOfTriangles];
		VO_Triangle2DStructure[] warpedTriangles = oShape.GetTriangle2DStructure(triangles);
		Rectangle iRect 	= iShape.GetShapeBoundRect();
		Rectangle oRect 	= oShape.GetShapeBoundRect();
		ImageData oImg 		= new ImageData(oRect.width, oRect.height, iImg.depth, VO_2DSMExplorer.palette);
		int[] vIdx 			= new int[3];
		
		// calculate all the possible mapping (for speeding up) 95 mapping totally
	    for (int i = 0; i < NbOfTriangles; i++ )
	    {
	    	matWarping[i] 	= new Matrix(2, 3);
			
	        // Get the affine transformation for each triangle pair.
			for(int j = 0; j < 3; j++)
			{
				vIdx[j] 	= triangles[i].GetVertexIndex(j);
				src[0][j]	= oShape.GetAShape(0, vIdx[j]);
				src[1][j]	= oShape.GetAShape(1, vIdx[j]);
				src[2][j]	= 1.0f;
				dst[0][j]	= iShape.GetAShape(0, vIdx[j]);
				dst[1][j]	= iShape.GetAShape(1, vIdx[j]);
			}
			Matrix m_src 	= new Matrix(src);
			Matrix m_dst 	= new Matrix(dst);
			matWarping[i] 	= m_dst.times(m_src.inverse());
	    }
		
		float XX, YY;
		Point2f pt = new Point2f();
		int thePixel = 0;
		
		if(NbOfChannels == 1)
		{
			for (int Y = 0; Y < oImg.height; Y++)
			{
				for (int X = 0; X < oImg.width; X++)
				{
					pt.x = X + iRect.x;
					pt.y = Y + iRect.y;
					
					int k = VO_Triangle2DStructure.IsPointInTriangles(pt, warpedTriangles);
					if(k>=0)
					{
						XX = (float) (matWarping[k].get(0,0)*pt.x + matWarping[k].get(0,1)*pt.y + matWarping[k].get(0,2));
						YY = (float) (matWarping[k].get(1,0)*pt.x + matWarping[k].get(1,1)*pt.y + matWarping[k].get(1,2));
						
						// Since the above calculations are basically matrix calculation, 
						// they won't be able to always ensure XX or YY are always within the image.
						// Therefore, a constrain in the following is a must!!!
						if (XX < 0.0f)
							XX = 0.0f;
						if (YY < 0.0f)
							YY = 0.0f;
						if (XX > ((float)(iRect.width - 1) - 0.001f))
							XX = (float) (iRect.width - 1) - 0.001f;
						if (YY > ((float)(iRect.height - 1) - 0.001f))
							YY = (float) (iRect.height - 1) - 0.001f;
						
	//						// warp from m_vTemplateTriangle2D[j] (template pixel in the specific triangle) to each shape/image
	//						warpsrc(0, 0) = pt.x;
	//						warpsrc(1, 0) = pt.y;
	//						warpsrc(2, 0) = 1.0;
	//
	//		//				cv::gemm(matWarping[k], warpsrc, 1.0, Mat(), 0.0, warpeddst);
	//						warpeddst = matWarping[k] * warpsrc;
	
						// extract the B,G,R on each shape/image respectively into textures
						// warpeddst.at<float>(0, 0) is for x while
						// warpeddst.at<float>(1, 0) is for y. But,
						// x determines cols, while y determines rows.
	//						VO_TextureModel::VO_CalcSubPixelTexture ( warpeddst(0, 0), warpeddst(1, 0), iImg, &gray);
						thePixel = (int) VO_TextureModel.VO_CalcSubPixelTexture(XX, YY, iImg)[0];
						oImg.setPixel(X, Y, thePixel);
						NbOfPixels ++;
					}
				}
			}
		}
		else if(NbOfChannels == 3)
		{
			float[] bgr = new float[3];
			for (int Y = 0; Y < oImg.height; Y++)
			{
				for (int X = 0; X < oImg.width; X++)
				{
					pt.x = X + iRect.x;
					pt.y = Y + iRect.y;
					
					int k = VO_Triangle2DStructure.IsPointInTriangles(pt, warpedTriangles);
					if(k>=0)
					{
						XX = (float) (matWarping[k].get(0,0)*pt.x + matWarping[k].get(0,1)*pt.y + matWarping[k].get(0,2));
						YY = (float) (matWarping[k].get(1,0)*pt.x + matWarping[k].get(1,1)*pt.y + matWarping[k].get(1,2));
						
						// Since the above calculations are basically matrix calculation, 
						// they won't be able to always ensure XX or YY are always within the image.
						// Therefore, a constrain in the following is a must!!!
						if (XX < 0.0f)
							XX = 0.0f;
						if (YY < 0.0f)
							YY = 0.0f;
						if (XX > ((float)(iRect.width - 1) - 0.001f))
							XX = (float) (iRect.width - 1) - 0.001f;
						if (YY > ((float)(iRect.height - 1) - 0.001f))
							YY = (float) (iRect.height - 1) - 0.001f;
						
	//						// warp from m_vTemplateTriangle2D[j] (template pixel in the specific triangle) to each shape/image
	//						warpsrc(0, 0) = pt.x;
	//						warpsrc(1, 0) = pt.y;
	//						warpsrc(2, 0) = 1.0;
	//
	//		//				cv::gemm(matWarping[k], warpsrc, 1.0, Mat(), 0.0, warpeddst);
	//						warpeddst = matWarping[k] * warpsrc;
						
						// extract the B,G,R on each shape/image respectively into textures
						// warpeddst.at<float>(0, 0) is for x while
						// warpeddst.at<float>(1, 0) is for y. But,
						// x determines cols, while y determines rows.
	//						VO_TextureModel::VO_CalcSubPixelTexture ( warpeddst(0, 0), warpeddst(1, 0), iImg, &gray);
						bgr = VO_TextureModel.VO_CalcSubPixelTexture(XX, YY, iImg);
						int b = (int)bgr[0];
						int g = (int)bgr[1];
						int r = (int)bgr[2];
						int b0 = b<<16;
						int g0 = g<<8;
						int r0 = r;
						thePixel = b0+g0+r0;
//						int thePixel1 = ((int)bgr[0] & 0xff)<<16 + ((int)bgr[1] & 0xff)<<8 + ((int)bgr[2] & 0xff);
//						thePixel = (b & 0xff)<<16
//									+(g & 0xff)<<8
//									+(r & 0xff);
						oImg.setPixel(X, Y, thePixel);
						NbOfPixels ++;
					}
				}
			}
		}
		
		return oImg;
    }

    /** Morphing */
	public static void                 	VO_Morphing(final VO_Shape iShape1, final VO_Shape iShape2, VO_Shape[] oShapes, final VO_Triangle2DStructure[] triangles, final ImageData iImg1, final ImageData iImg2, ImageData[] oImgs, float step)
    {
		assert(iImg1.width == iImg2.width && iImg1.height == iImg2.height );
	    int NbOfFrames     = Math.round(1.0f/step);
	    oShapes = new VO_Shape[NbOfFrames+1];
	    oImgs = new ImageData[NbOfFrames+1];

	    ImageData morph1 = new ImageData(iImg1.width, iImg1.height, iImg1.depth, VO_2DSMExplorer.palette);
	    ImageData morph2 = new ImageData(iImg1.width, iImg1.height, iImg1.depth, VO_2DSMExplorer.palette);

	    oShapes[0]              	= iShape1;
	    oShapes[NbOfFrames]     	= iShape2;
	    oImgs[0]					= iImg1;
	    oImgs[NbOfFrames]			= iImg2;
	    VO_Shape tmpShape1 = new VO_Shape();
	    VO_Shape tmpShape2 = new VO_Shape();

	    for(int i = 1; i < NbOfFrames; ++i)
	    {
	    	tmpShape1 = iShape1.multiply(step*i);
	    	tmpShape2 = iShape2.multiply(1.0f-step*i);
	    	oShapes[i] = tmpShape1.add(tmpShape2);
	    	morph1 = VO_TextureModel.VO_WarpFromOneShapeToAnother(iShape1, oShapes[i], triangles, iImg1);
	    	morph2 = VO_TextureModel.VO_WarpFromOneShapeToAnother(iShape2, oShapes[i], triangles, iImg2);
//	        cv::addWeighted( morph1, step*i, morph2, 1.0-step*i, 0.0, oImgs[i] );
	    }
    }

    /** Put one texture to whatever shape, just for display */
	public static void                 	VO_PutOneTextureToOneShape(final VO_Texture texture, final VO_Shape oShape, final VO_Triangle2DStructure[] triangles, ImageData oImg)
    {
		ImageData intermediateImg = (ImageData) oImg.clone();
	    VO_TextureModel.VO_PutOneTextureToTemplateShape(texture, triangles, intermediateImg);
	    VO_Shape intermediateShape = VO_Triangle2DStructure.Triangle2D2Shape(triangles);
	    oImg = VO_TextureModel.VO_WarpFromOneShapeToAnother(intermediateShape, oShape, triangles, intermediateImg);
    }

    /** Warp form one shape to another */
	public static float[]        		VO_CalcSubPixelTexture(float x, float y, final ImageData image)
    {
		float[] result = new float[3];

	    int Y0 		= (int)y;
	    int X0 		= (int)x;
	    int Y1 		= (int)(y+1.0f);
	    int X1 		= (int)(x+1.0f);

	    float s		= x-(float)X0;
	    float t		= y-(float)Y0;
		float s1	= 1.0f-s;
		float t1	= 1.0f-t;
		
		RGB[] rgb = new RGB[4];
	    
	    rgb[0] = image.palette.getRGB(image.getPixel(X0, Y0));
	    rgb[1] = image.palette.getRGB(image.getPixel(X0, Y1));
	    rgb[2] = image.palette.getRGB(image.getPixel(X1, Y0));
	    rgb[3] = image.palette.getRGB(image.getPixel(X1, Y1));
	    
	    float tempAB, tempAG, tempAR;
		float tempBB, tempBG, tempBR;
		
		tempAB = t1 * rgb[0].blue + t * rgb[1].blue;
		tempBB = t1 * rgb[2].blue + t * rgb[3].blue;
		result[0] = tempAB * s1 + tempBB * s;
		tempAG = t1 * rgb[0].green + t * rgb[1].green;
		tempBG = t1 * rgb[2].green + t * rgb[3].green;
		result[1] = tempAG * s1 + tempBG * s;
		tempAR = t1 * rgb[0].red + t * rgb[1].red;
		tempBR = t1 * rgb[2].red + t * rgb[3].red;
		result[2] = tempAR * s1 + tempBR * s;

		return result;
    }
//	public static void                 	VO_CalcSubPixelTexture(float x, float y, final ImageData image, float gray)
//    {
//    }
//	public static void                 	VO_CalcSubPixelTexture(float x, float y, final ImageData image, float[] bgr)
//    {
//    }

    /** Get an intensity vector of size 1 or 3 (rgb), at point (x,y) in image */
//	public static void					VO_CalcPixelRGB(int x, int y, final ImageData image, float B, float G, float R)
//    {
//    	
//    }

    /** Change the normalized texture inTexture to the reference one outTexture */
	public static void                 	VO_NormalizedTexture2ReferenceScale(final VO_Texture inTexture, float textureSD, VO_Texture outTexture)
    {
		outTexture = new VO_Texture(inTexture);
	    outTexture.Scale(textureSD);										// scale first
	    outTexture.Shift(com.visionopen.jiapei.utils.Common.textureShift);	// shift second

		outTexture.Clamp(0.0f, 255.0f);
    }

    /** Normalize inTexture to outTexture */
	public static void                 	VO_ReferenceTextureBack2Normalize(final VO_Texture inTexture, float textureSD, VO_Texture outTexture)
    {
		outTexture = new VO_Texture(inTexture);
		outTexture.Shift(-com.visionopen.jiapei.utils.Common.textureShift);	// shift first
		outTexture.Scale(1.0f/textureSD);										// scale second
    }

    /** Put edges on template face */
	public static void                 	VO_PutEdgesOnTemplateFace(final VO_Edge[] edges, final VO_Shape templateShape, final ImageData iImg, ImageData oImg)
    {
    	
    }

    /** Put every single triangle onto iImg and obtaining oImg */
	public static void                 	VO_PutTrianglesOnTemplateFace(final VO_Triangle2DStructure[] triangles, final ImageData iImg, ImageData[] oImgs)
    {
    	
    }
	
	/** Put every single ellipse onto iImg and obtaining oImg */
    public static void               	VO_PutPDMEllipsesOnTemplateFace(final Ellipse2D[] ellipses, final ImageData iImg, ImageData oImg)
    {
    	
    }

    /** Put a shape onto iImg and obtaining oImg, without the edge information */
	public static void                 	VO_PutShapeOnTemplateFace(final VO_Shape iShape, final ImageData iImg, ImageData oImg)
    {
	    int NbOfPoints =  iShape.GetNbOfPoints();
	    oImg = (ImageData) iImg.clone();
	    Point2f pt;

	    for(int i = 0; i < NbOfPoints; i++)
	    {
	        pt	= iShape.GetA2DPoint(i);
	        
	        oImg.setPixel( (int)pt.x, (int)pt.y, 0xFF);
	    }
    }
    
    /** Texture parameters constraints */
    public void							VO_TextureParameterConstraint(float[] ioT, float nSigma)
    {
    	int NbOfTextureParameterConstraints = ioT.length;
    	float ct = 0.0f;

        for (int i = 0; i < NbOfTextureParameterConstraints; ++i)
        {
        	ct = nSigma * (float) Math.sqrt(this.m_PCANormalizedTexture.eigenvalues[i]);
            if ( ioT[i] > ct )
            {
            	ioT[i] = ct;
            }
            else if ( ioT[i] < -ct )
            {
            	ioT[i] = -ct;
            }
        }
    }
    
    /** Texture projected to texture parameters*/
    public void 						VO_NormalizedTextureProjectToTParam(final VO_Texture iTexture, float[] outT)
    {
    	this.m_PCANormalizedTexture.project(iTexture.GetTheTextureInARow(), outT);
    }

    /** Texture parameters back projected to texture parameters */
    public void	                   		VO_TParamBackProjectToNormalizedTexture(final float[] inT, VO_Texture oTexture, int tr)
    {
    	oTexture.SetTheTexture(this.m_PCANormalizedTexture.backProject(inT), tr);
    }
    
    public void 						VO_TParamBackProjectToNormalizedTexture(final float[] inT, float[] oTextureMat)
    {
    	oTextureMat = this.m_PCANormalizedTexture.backProject(inT);
    	// The following lines are must !!! Commented by JIA Pei
		for(int i = 0; i < oTextureMat.length; i++)
		{
			if ( oTextureMat[i] <= 0.0)
				oTextureMat[i] = (float) 0.0;
			else if (oTextureMat[i] >= 255.0)
				oTextureMat[i] = (float) 255.0;
		}
    }

    /** texture -> normalized -> project to texture parameters */
    public void   	                    VO_CalcAllParams4AnyTexture(final float[][] iTexture, float[][] oTexture, float[] outT)
    {
    	int NbOfTextureRepresentation = iTexture.length;
    	// Here, for VO_Texture; there is no point to know how many texture representations for each VO_Texture
    	VO_Texture oT = new VO_Texture(iTexture);
    	oT.Normalize();
    	this.m_PCANormalizedTexture.project(oT.GetTheTextureInARow(), outT );
    	oTexture = com.visionopen.jiapei.utils.Common.reshapeTo2D(oT.GetTheTextureInARow(), NbOfTextureRepresentation);
    }

    /** texture -> normalized -> project to texture parameters */
    public void                         VO_CalcAllParams4AnyTexture(VO_Texture ioTexture, float[] outT)
    {
        ioTexture.Normalize();
    	this.m_PCANormalizedTexture.project(ioTexture.GetTheTextureInARow(), outT );
    }

	/** Load Training data for texture model */
    public boolean						VO_LoadTextureTrainingData( final String[] allImgFiles4Training,
																	int channels,
																	int trm)
	{
		return false;
	}

    /** Build Texture Model */
    public void                         VO_BuildTextureModel(final String[] allLandmarkFiles4Training,
															final String[] allImgFiles4Training,
															final String shapeinfoFileName, 
															int database,
															int channels,
															int trm, 
															float TPShape, 
															float TPTexture, 
															boolean useKnownTriangles)
    {
    	
    }

    /** Save Texture Model, to a specified folder */
    public void                    		VO_Save(final String fd)
    {
    	
    }

    /** Load all parameters */
    public void                     	VO_LoadOthers(final String fd)
    {
		if(fd != "")
		{
			super.VO_LoadOthers(fd);
		    String folderDir = fd+"/TextureModel";
		    String filename = new String();
		    FileReader filereader = null;
		    BufferedReader in = null;
		    String str = new String();
		    String ss[] = null;
	
			// m_vTextures
			try {
				filename = folderDir + "/m_vTextures.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vTextures = new VO_Texture[this.m_iNbOfSamples];
		        
		        for(int i = 0; i < this.m_iNbOfSamples; i++)
		        {
		        	this.m_vTextures[i] = new VO_Texture(this.m_iNbOfTextureRepresentations, this.m_iNbOfPixels);
		        	for(int j = 0; j < this.m_iNbOfTextureRepresentations; j++)
		        	{
				        while ( (str = in.readLine())!=null )
				        {
				        	if(str.trim().length() != 0)
				        	{
						        ss = str.split(" ");
						        for(int k = 0; k < this.m_iNbOfPixels; k++)
						        {
						        	this.m_vTextures[i].SetATexture(j, k, Float.parseFloat(ss[k]));
						        }
						        break;
				        	}
				        }
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        	
			} catch (IOException e){
				e.printStackTrace();
			}
	
		    // m_vNormalizedTextures
			try {
				filename = folderDir + "/m_vNormalizedTextures.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vNormalizedTextures = new VO_Texture[this.m_iNbOfSamples];
		        
		        for(int i = 0; i < this.m_iNbOfSamples; i++)
		        {
		        	this.m_vNormalizedTextures[i] = new VO_Texture(this.m_iNbOfTextureRepresentations, this.m_iNbOfPixels);
		        	for(int j = 0; j < this.m_iNbOfTextureRepresentations; j++)
		        	{
				        while ( (str = in.readLine())!=null )
				        {
				        	if(str.trim().length() != 0)
				        	{
						        ss = str.split(" ");
						        for(int k = 0; k < this.m_iNbOfPixels; k++)
						        {
						        	this.m_vNormalizedTextures[i].SetATexture(j, k, Float.parseFloat(ss[k]));
						        }
						        break;
				        	}
				        }
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		    
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

    /** Load Parameters for fitting */
    public void                     	VO_LoadParameters4Fitting(final String fd)
    {
		if(fd != "")
		{
			init();
			super.VO_LoadParameters4Fitting(fd);
			String folderDir = fd+"/TextureModel";
		    String filename = new String();
		    FileReader filereader = null;
		    BufferedReader in = null;
		    String str = new String();
		    String ss[] = null;
		    
		    // Read TextureModel.txt
			try {
				filename = folderDir + "/TextureModel.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iTextureRepresentationMethod = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfTextureRepresentations = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfChannels = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfPixels = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfTextures = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfEigenTexturesAtMost = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_iNbOfTextureEigens = Integer.parseInt(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_fAverageTextureStandardDeviation = Float.parseFloat(str);
		        
		        str = in.readLine();
		        str = in.readLine();
		        this.m_fTruncatedPercent_Texture = Float.parseFloat(str);
		        
		        in.close();
		        filereader.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    
		    this.m_PCANormalizedTexture = new PCA();
		    
		    // m_PCANormalizedTextureMean
			try {
				filename = folderDir + "/m_PCANormalizedTextureMean.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCANormalizedTexture.mean = new float[this.m_iNbOfTextures];
		        
		        if ( (str = in.readLine())!=null)
		        {
		        	ss = str.split(" ");
		        	assert (this.m_iNbOfTextures == ss.length);
		        	for(int i = 0; i < this.m_iNbOfTextures; i++)
		        	{
		        		this.m_PCANormalizedTexture.mean[i] = Float.parseFloat(ss[i]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}

			// m_PCANormalizedTextureEigenValues
			try {
				filename = folderDir + "/m_PCANormalizedTextureEigenValues.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCANormalizedTexture.eigenvalues = new float[this.m_iNbOfTextureEigens];
		        
	        	for(int i = 0; i < this.m_iNbOfTextureEigens; i++)
	        	{
	        		if ( (str = in.readLine())!=null)
	        		this.m_PCANormalizedTexture.eigenvalues[i] = Float.parseFloat(str);
	        	}
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
			// m_PCANormalizedTextureEigenVectors
			try {
				filename = folderDir + "/m_PCANormalizedTextureEigenVectors.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_PCANormalizedTexture.eigenvectors = new float[this.m_iNbOfTextureEigens][this.m_iNbOfTextures];

		        for(int i = 0; i < this.m_iNbOfTextureEigens; i++)
		        {
		        	if ( (str = in.readLine())!=null)
			        	ss = str.split(" ");
	        		assert (this.m_iNbOfTextures == ss.length);
		        	for(int j = 0; j < this.m_iNbOfTextures; j++)
		        	{
		        		this.m_PCANormalizedTexture.eigenvectors[i][j] = Float.parseFloat(ss[j]);
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
		    // m_VONormalizedMeanTexture
			try {
				filename = folderDir + "/m_VONormalizedMeanTexture.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_VONormalizedMeanTexture = new VO_Texture(3, this.m_iNbOfPixels);
		        for(int i = 0; i < this.m_iNbOfTextureRepresentations; i++)
		        {
		        	if ( (str = in.readLine())!=null )
			        {
				        ss = str.split(" ");
				        for(int j = 0; j < this.m_iNbOfPixels; j++)
				        {
				        	this.m_VONormalizedMeanTexture.SetATexture(i, j, Float.parseFloat(ss[j]));
				        }
			        }
		        }
		        
		        in.close();
		        filereader.close();
		        	
			} catch (IOException e){
				e.printStackTrace();
			}
			
		    // m_VOReferenceTexture
			try {
				filename = folderDir + "/m_VOReferenceTexture.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_VOReferenceTexture = new VO_Texture(3, this.m_iNbOfPixels);
		        for(int i = 0; i < this.m_iNbOfTextureRepresentations; i++)
		        {
		        	if ( (str = in.readLine())!=null )
			        {
				        ss = str.split(" ");
				        for(int j = 0; j < this.m_iNbOfPixels; j++)
				        {
				        	this.m_VOReferenceTexture.SetATexture(i, j, Float.parseFloat(ss[j]));
				        }
						break;
			        }
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
			// m_vTemplatePointWarpInfo
			try {
				filename = folderDir + "/m_vTemplatePointWarpInfo.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vTemplatePointWarpInfo = new VO_WarpingPoint[this.m_iNbOfPixels];

		        for(int i = 0; i < this.m_iNbOfPixels; i++)
		        {
		        	this.m_vTemplatePointWarpInfo[i] = new VO_WarpingPoint();
		        	if ( (str = in.readLine())!=null)
		        	{
			        	ss = str.split(" ");
			        	this.m_vTemplatePointWarpInfo[i].SetPointIndex(Integer.parseInt(ss[0]));
			        	this.m_vTemplatePointWarpInfo[i].SetPosition(Float.parseFloat(ss[1]), Float.parseFloat(ss[2]));
			        	this.m_vTemplatePointWarpInfo[i].SetTriangleIndex(Integer.parseInt(ss[3]));
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
			// m_vNormalizedPointWarpInfo
			try {
				filename = folderDir + "/m_vNormalizedPointWarpInfo.txt";
				filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();	// For the 1st line, the title, but not the data
		        this.m_vNormalizedPointWarpInfo = new VO_WarpingPoint[this.m_iNbOfPixels];

		        for(int i = 0; i < this.m_iNbOfPixels; i++)
		        {
		        	this.m_vNormalizedPointWarpInfo[i] = new VO_WarpingPoint();
		        	if ( (str = in.readLine())!=null)
		        	{
			        	ss = str.split(" ");
			        	this.m_vNormalizedPointWarpInfo[i].SetPointIndex(Integer.parseInt(ss[0]));
			        	this.m_vNormalizedPointWarpInfo[i].SetPosition(Float.parseFloat(ss[1]), Float.parseFloat(ss[2]));
			        	this.m_vNormalizedPointWarpInfo[i].SetTriangleIndex(Integer.parseInt(ss[3]));
		        	}
		        }
		        
		        in.close();
		        filereader.close();
		        
			} catch (IOException e){
				e.printStackTrace();
			}
			
		    /** Template face image */
			filename = folderDir + "/Reference.jpg";
			ImageLoader loader = new ImageLoader();
			this.m_ImageTemplateFace = loader.load(filename)[0];
		}
    }

    /** Gets and Sets */
    public float[]                 		GetNormalizedTextureMean() {return this.m_PCANormalizedTexture.mean;}
    public float[]                 		GetNormalizedTextureEigenValues() {return this.m_PCANormalizedTexture.eigenvalues;}
	public float[][]               		GetNormalizedTextureEigenVectors() {return this.m_PCANormalizedTexture.eigenvectors;}
	public int                    		GetTextureExtractionMethod() {return this.m_iTextureRepresentationMethod;}
	public int                    		GetNbOfTextureRepresentations() {return this.m_iNbOfTextureRepresentations;}
	public int                    		GetNbOfChannels() {return this.m_iNbOfChannels;}
	public int                    		GetNbOfPixels() {return this.m_iNbOfPixels;}
	public int                    		GetNbOfTextures() {return this.m_iNbOfTextures;}
	public int                    		GetNbOfEigenTexturesAtMost() {return this.m_iNbOfEigenTexturesAtMost;}
	public int                    		GetNbOfTextureEigens() {return this.m_iNbOfTextureEigens;}
	public VO_Texture					GetNormalizedMeanTexture() {return this.m_VONormalizedMeanTexture;}
	public VO_Texture                 	GetAAMReferenceTexture() {return this.m_VOReferenceTexture;}
	public float                        GetAverageTextureStandardDeviation() {return this.m_fAverageTextureStandardDeviation;}
	public float                        GetTruncatedPercent_Texture() {return this.m_fTruncatedPercent_Texture;}
	public VO_Texture[]         		GetTextures() {return this.m_vTextures;}
	public VO_Texture[]         		GetNormalizedTextures() {return this.m_vNormalizedTextures;}
	public ImageData               		GetIplTemplateFace() {return this.m_ImageTemplateFace;}
	public ImageData              		GetIplEdges() {return this.m_ImageEdges;}
    public VO_WarpingPoint[]      		GetTemplatePointWarpInfo() {return this.m_vTemplatePointWarpInfo;}
    public VO_WarpingPoint[]      		GetNormalizedPointWarpInfo() {return this.m_vNormalizedPointWarpInfo;}
    public String[]						GetStringTrainingImageNames() {return this.m_vStringTrainingImageNames;}

    public static void main(String[] args) {
    	String fd = "/home/jiapei/MyPrograms/Eclipse/AAMJava3D/traineddata/2D/JIAPei";
    	VO_TextureModel texturemodel = new VO_TextureModel();
    	texturemodel.VO_LoadParameters4Fitting(fd);
    	texturemodel.VO_LoadOthers(fd);
	}
}
