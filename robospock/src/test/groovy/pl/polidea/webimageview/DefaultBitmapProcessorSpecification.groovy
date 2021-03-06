package pl.polidea.webimageview

import static pl.polidea.webimageview.processor.Processor.ProcessorType.FIX_BOTH
import static pl.polidea.webimageview.processor.Processor.ProcessorType.FIX_HEIGHT
import static pl.polidea.webimageview.processor.Processor.ProcessorType.FIX_WIDTH
import static pl.polidea.webimageview.processor.Processor.ProcessorType.ORIGNAL

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import com.xtremelabs.robolectric.Robolectric
import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory
import pl.polidea.imagecache.shadows.HighDensityShadowResources
import pl.polidea.imagecache.shadows.LayoutHeightWidthAwareShadowTypedArray
import pl.polidea.imagecache.shadows.MyShadowActivityManager
import pl.polidea.imagecache.shadows.MyShadowBitmap
import pl.polidea.imagecache.shadows.MyShadowBitmapFactory
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import pl.polidea.webimagesampleapp.R
import pl.polidea.webimageview.processor.Processor
import spock.lang.Unroll

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@pl.polidea.pl>
 */
@UseShadows([MyShadowActivityManager, HighDensityShadowResources, LayoutHeightWidthAwareShadowTypedArray])
class DefaultBitmapProcessorSpecification extends RoboSpecification {

    def LayoutInflater inflater;
    def name = "testBitmapName"
    def file = new File(name)

    def setup() {
        inflater = LayoutInflater.from(Robolectric.application);
        MyShadowBitmap.shouldThrowException = false
        MyShadowBitmapFactory.shouldThrowException = false
        file.createNewFile()
    }

    def cleanup() {
        file.delete()
        ShadowBitmapFactory.reset()
    }

    @Unroll
    def "should have #expectedProcessorType for id #viewResId"() {
        when:
        LayoutHeightWidthAwareShadowTypedArray.viewID = viewResId;
        Processor.ProcessorType type = getProcessor(viewResId).determineProcessor().type;

        then:
        type == expectedProcessorType;

        where:
        viewResId                | expectedProcessorType
        R.id._wrap_contents      | ORIGNAL
        R.id._match_parents      | ORIGNAL
        R.id._fixed_width_wrap   | FIX_WIDTH
        R.id._fixed_width_match  | FIX_WIDTH
        R.id._fixed_height_wrap  | FIX_HEIGHT
        R.id._fixed_height_match | FIX_HEIGHT
        R.id._fixed_both         | FIX_BOTH
    }

    def "should have ORIGINAL type by default, when no attributes passed to processor"() {
        given:
        def noAttrsSet = null
        final DefaultBitmapProcessor processor = new DefaultBitmapProcessor(Robolectric.application, noAttrsSet);

        when:
        final Processor.ProcessorType type = processor.determineProcessor().type;

        then:
        ORIGNAL == type
    }

    @Unroll
    def "should be w:#bitmapWidth h:#bitmapHeight for input w:#inputWidth h:#inputHeight and processor type #processorTypeAsString"() {
        when:
        LayoutHeightWidthAwareShadowTypedArray.viewID = viewResId;
        final DefaultBitmapProcessor processor = getProcessor(viewResId);
        ShadowBitmapFactory.provideWidthAndHeightHints(file.getPath(), inputWidth, inputHeight);
        final Bitmap bitmap = processor.process(file);

        then:
        bitmap.getWidth() == bitmapWidth
        bitmap.getHeight() == bitmapHeight

        where:
        processorTypeAsString | viewResId               | inputWidth | inputHeight | bitmapWidth | bitmapHeight
        "_dips_and_pix"       | R.id._dips_and_pix      | 100        | 80          | 25          | 20
        "_wrap_contents"      | R.id._wrap_contents     | 50         | 60          | 50          | 60
        "_fixed_width_wrap"   | R.id._fixed_width_wrap  | 100        | 80          | 60          | 48
        "_fixed_height_wrap"  | R.id._fixed_height_wrap | 100        | 80          | 75          | 60
        "_fixed_both"         | R.id._fixed_both        | 100        | 80          | 46          | 37
    }

    DefaultBitmapProcessor getProcessor(final int id) {
        final WebImageView view = getView(id);
        // TODO: place here implementation of reading xml
        // TODO: arguments can be passed via Mockito
        return view.getBitmapProcessor();
    }

    WebImageView getView(final int id) {
        final View inflate = inflater.inflate(R.layout.for_tests, null);
        return (WebImageView) inflate.findViewById(id);
    }
}
