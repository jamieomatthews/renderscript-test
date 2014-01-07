
#pragma version(1)
#pragma rs java_package_name(com.dss.renderscripttest)

float brightnessValue;

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

static int mImageWidth;
const uchar4 *gPixels;

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
	
	float4 apixel = rsUnpackColor8888(*v_in);
    float3 pixel = apixel.rgb;
    float factor = brightnessValue;
    pixel = pixel + factor;
    pixel = clamp(pixel,0.0f,1.0f);
    *v_out = rsPackColorTo8888(pixel.rgb);
}


void filter() {
    mImageWidth = rsAllocationGetDimX(gIn);
    rsDebug("Image size is ", rsAllocationGetDimX(gIn), rsAllocationGetDimY(gOut));
    rsForEach(gScript, gIn, gOut, 0, 0);
}