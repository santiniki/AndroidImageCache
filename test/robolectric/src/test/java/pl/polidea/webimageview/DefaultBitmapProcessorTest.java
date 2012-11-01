package pl.polidea.webimageview;

import static org.junit.Assert.*;
import static pl.polidea.webimageview.DefaultBitmapProcessor.Type.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;

import pl.polidea.imagecache.*;
import pl.polidea.webimageview.DefaultBitmapProcessor.Type;
import android.graphics.*;
import android.view.*;

import com.xtremelabs.robolectric.*;
import com.xtremelabs.robolectric.shadows.*;

@RunWith(ImageCacheTestRunner.class)
public class DefaultBitmapProcessorTest {

	LayoutInflater inflater;
	File f = new File("file");

	@Before
	public void setup() {
		inflater = LayoutInflater.from(Robolectric.application);
	}

	@Test
	public void testProcessingTwoWrapContent() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._wrap_contents);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingTwoMatchParent() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._match_parents);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingFixedWidthWithWrapConent() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_width_wrap);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingFixedWidthWithMatchParent() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_width_match);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingFixedHeightWithWrapContent() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_height_wrap);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingFixedHeightWithMatchParent() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_height_match);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingFixedHeightAndWidth() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingDipsAndPixels() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._dips_and_pix);

		// when
		final Type type = processor.determineType();

		// then
		assertEquals(ORIGNAL, type);
	}

	@Test
	public void testProcessingOriginal() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._wrap_contents);
		final int width = 50;
		final int height = 60;
		ShadowBitmapFactory.provideWidthAndHeightHints(f.getPath(), width, height);

		// when
		final Bitmap bitmap = processor.process(f);

		// then
		assertEquals(50, bitmap.getWidth());
		assertEquals(60, bitmap.getHeight());
	}

	@Test
	public void testProcessingFixedWidth() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_width_wrap);
		final int width = 100;
		final int height = 80;
		ShadowBitmapFactory.provideWidthAndHeightHints(f.getPath(), width, height);

		// when
		final Bitmap bitmap = processor.process(f);

		// then
		assertEquals(25, bitmap.getWidth());
		assertEquals(20, bitmap.getHeight());
	}

	@Test
	public void testProcessingFixedHeight() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_height_wrap);
		final int width = 100;
		final int height = 80;
		ShadowBitmapFactory.provideWidthAndHeightHints(f.getPath(), width, height);

		// when
		final Bitmap bitmap = processor.process(f);

		// then
		assertEquals(25, bitmap.getWidth());
		assertEquals(20, bitmap.getHeight());
	}

	@Test
	public void testProcessingFixedBoth() {
		// given
		final DefaultBitmapProcessor processor = getProcessor(R.id._fixed_both);
		final int width = 100;
		final int height = 80;
		ShadowBitmapFactory.provideWidthAndHeightHints(f.getPath(), width, height);

		// when
		final Bitmap bitmap = processor.process(f);

		// then
		assertEquals(31, bitmap.getWidth());
		assertEquals(25, bitmap.getHeight());
	}

	DefaultBitmapProcessor getProcessor(final int id) {
		final WebImageView view = getView(id);
		return new DefaultBitmapProcessor(view);
	}

	WebImageView getView(final int id) {
		final View inflate = inflater.inflate(R.layout.for_tests, null);
		return (WebImageView) inflate.findViewById(id);
	}
}
