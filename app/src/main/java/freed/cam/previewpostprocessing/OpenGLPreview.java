package freed.cam.previewpostprocessing;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;

import freed.cam.histogram.HistogramController;
import freed.gl.GLPreview;
import freed.gl.PreviewModel;
import freed.settings.SettingKeys;
import freed.settings.SettingsManager;
import freed.utils.DisplayUtil;
import freed.viewer.screenslide.views.MyHistogram;

public class OpenGLPreview implements Preview, TextureView.SurfaceTextureListener
{

    private GLPreview glPreview;
    private PreviewEvent previewEventListner;
    private HistogramController histogramController;

    public OpenGLPreview(Context context, HistogramController myHistogram)
    {
        glPreview = new GLPreview(context);
        glPreview.setSurfaceTextureListener(this);
    }

    @Override
    public void close() {
    }

    @Override
    public SurfaceTexture getSurfaceTexture() {
        return glPreview.getSurfaceTexture();
    }

    @Override
    public Surface getInputSurface() {
        return null;
    }

    @Override
    public void setOutputSurface(Surface surface) {

    }

    @Override
    public void setSize(int width, int height) {
        Point disp =DisplayUtil.getDisplaySize();
        glPreview.scale(width,height,disp.x,disp.y, SettingsManager.get(SettingKeys.SWITCH_ASPECT_RATIO).get());
    }

    @Override
    public boolean isSucessfullLoaded() {
        return true;
    }

    @Override
    public void setBlue(boolean blue) {
        glPreview.setBlue(blue);
    }

    @Override
    public void setRed(boolean red) {
        glPreview.setRed(red);
    }

    @Override
    public void setGreen(boolean green) {
        glPreview.setGreen(green);
    }

    @Override
    public void setFocusPeak(boolean on) {
        glPreview.setFocuspeak_enabled(on);
    }

    @Override
    public boolean isFocusPeak() {
        return glPreview.isFocuspeak_enabled();
    }

    @Override
    public void setClipping(boolean on) {
        glPreview.setZebra_enabled(on);
    }

    @Override
    public boolean isClipping() {
        return glPreview.isZebra_enabled();
    }

    @Override
    public void setHistogram(boolean on) {

    }

    @Override
    public boolean isHistogram() {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public View getPreviewView() {
        return glPreview;
    }

    @Override
    public void setPreviewEventListner(PreviewEvent eventListner) {
        this.previewEventListner = eventListner;
    }

    @Override
    public int getPreviewWidth() {
        return glPreview.getWidth();
    }

    @Override
    public int getPreviewHeight() {
        return glPreview.getHeight();
    }

    @Override
    public void setRotation(int width, int height, int rotation) {
        glPreview.setOrientation(rotation);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        if (previewEventListner != null)
            previewEventListner.onPreviewAvailable(surface,width,height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        if (previewEventListner != null)
            previewEventListner.onPreviewSizeChanged(surface,width,height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        if (previewEventListner != null)
            previewEventListner.onPreviewDestroyed(surface);
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        if (previewEventListner != null)
            previewEventListner.onPreviewUpdated(surface);
    }
}
