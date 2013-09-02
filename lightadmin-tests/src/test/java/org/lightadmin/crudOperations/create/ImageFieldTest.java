package org.lightadmin.crudOperations.create;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightadmin.LoginOnce;
import org.lightadmin.RunWithConfiguration;
import org.lightadmin.SeleniumIntegrationTest;
import org.lightadmin.component.QuickViewComponent;
import org.lightadmin.component.WarningDialog;
import org.lightadmin.config.TestProductConfiguration;
import org.lightadmin.data.Domain;
import org.lightadmin.page.CreatePage;
import org.lightadmin.page.ListViewPage;
import org.lightadmin.page.ShowViewPage;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.annotation.SystemProfileValueSource;

import java.awt.*;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.lightadmin.util.DomainAsserts.assertImagePreviewIsDisplayed;

@Ignore
@RunWithConfiguration( { TestProductConfiguration.class } )
@LoginOnce( domain = Domain.TEST_PRODUCTS )
@ProfileValueSourceConfiguration( SystemProfileValueSource.class )
@IfProfileValue( name = "os.name", value = "Linux" )
public class ImageFieldTest extends SeleniumIntegrationTest {

	private CreatePage createPage;
	private WarningDialog warningDialog;
	private ShowViewPage showView;
	private ListViewPage listView;
	private QuickViewComponent quickView;

	@Before
	public void setup() throws Exception {
		getStartPage().navigateToDomain( Domain.TEST_PRODUCTS );
		createPage = getStartPage().navigateToCreatePage();
	}

	@After
	public void cleanup() {
		repopulateDatabase();
	}

	@Test
	public void fileFormatIsValidated() throws AWTException, IOException {

		warningDialog = createPage.addInvalidFile( invalidFileFormatName );

		assertTrue( "File Format validation warning is not displayed", warningDialog.isPresent() );
		assertEquals(
				String.format( "Selected file type is not supported (%s). Please select JPG or PNG file.", invalidFileFormatName ),
				warningDialog.getMessage() );

		warningDialog.close();
	}

	@Test
	public void fileSizeIsValidated() throws AWTException, IOException {

		warningDialog = createPage.addInvalidFile( largeFileName );

		assertTrue( "File Size validation warning is not displayed", warningDialog.isPresent() );
		assertEquals(
				String.format( "Selected file %s exceeds file size limit of 10MB", largeFileName ),
				warningDialog.getMessage() );

		warningDialog.close();
	}

	@Test
	public void jpegFileCanBeUploaded() throws IOException, AWTException {
		final String itemName = "Test Jpg";

		createPage.type( "name", itemName );
		createPage.addValidFile( jpgFileName );

		showView = createPage.submit();
		assertImagePreviewIsDisplayed( "Show View", showView.getField( "picture" ), webDriver(), webDriverTimeout() );

		listView = showView.navigateToDomain( Domain.TEST_PRODUCTS );
		assertImagePreviewIsDisplayed( "List View", listView.getRowForItem( itemName ), webDriver(), webDriverTimeout() );

		quickView = listView.showQuickViewForItem( itemName );
		assertImagePreviewIsDisplayed( "Quick View", quickView.getField( "Picture" ), webDriver(), webDriverTimeout() );
	}

	@Test
	public void pngFileCanBeUploaded() throws IOException, AWTException {
		final String itemName = "Test Png";

		createPage.type( "name", itemName );
		createPage.addValidFile( pngFileName );

		showView = createPage.submit();
		assertImagePreviewIsDisplayed( "Show View", showView.getField( "picture" ), webDriver(), webDriverTimeout() );

		listView = showView.navigateToDomain( Domain.TEST_PRODUCTS );
		assertImagePreviewIsDisplayed( "List View", listView.getRowForItem( itemName ), webDriver(), webDriverTimeout() );

		quickView = listView.showQuickViewForItem( itemName );
		assertImagePreviewIsDisplayed( "Quick View", quickView.getField( "Picture" ), webDriver(), webDriverTimeout() );
	}

	@Test
	public void cmykFileCanBeUploaded() throws IOException, AWTException {
		final String itemName = "Test CMYK";

		createPage.type( "name", itemName );
		createPage.addValidFile( cmykFileName );

		showView = createPage.submit();
		assertImagePreviewIsDisplayed( "Show View", showView.getField( "picture" ), webDriver(), webDriverTimeout() );

		listView = showView.navigateToDomain( Domain.TEST_PRODUCTS );
		assertImagePreviewIsDisplayed( "List View", listView.getRowForItem( itemName ), webDriver(), webDriverTimeout() );

		quickView = listView.showQuickViewForItem( itemName );
		assertImagePreviewIsDisplayed( "Quick View", quickView.getField( "Picture" ), webDriver(), webDriverTimeout() );
	}

	private static final String largeFileName = "largeFile.jpg";
	private static final String invalidFileFormatName = "invalidFormat.gif";
	private static final String jpgFileName = "validFile.jpg";
	private static final String pngFileName = "validFile.png";
	private static final String cmykFileName = "cmykFile.jpg";
}
