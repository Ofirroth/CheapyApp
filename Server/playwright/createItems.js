const { chromium } = require('playwright');
const Item = require('../models/item');
const Barcode = require('../models/barcode');
const updatePricesFromStores = require('./getPrices');

async function scrapeAndSaveItems() {
  const browser = await chromium.launch({ headless: false, slowMo: 100 });
  const page = await browser.newPage();

  try {
    console.log('Fetching barcodes from the database...');
    const barcodes = await Barcode.find({});
    if (!barcodes.length) {
      console.log('No barcodes found in the database.');
      return;
    }

    console.log('Opening the website...');
    const url = 'https://shop.hazi-hinam.co.il/';
    await page.goto(url, { waitUntil: 'domcontentloaded' });

    for (const barcodeDoc of barcodes) {
      const barcode = barcodeDoc.barcode;
      try {
        console.log(`Starting search for barcode: ${barcode}`);

        await page.getByRole('button', { name: 'חיפוש' }).click();
        console.log('Clicked the search button to activate the input field.');

        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').fill(barcode);
        console.log(`Filled search input with barcode: ${barcode}`);
        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').press('Enter');
        console.log('Pressed Enter to trigger the search.');

        const productSelector = '#productStripInfo';
        const productSelectorName = '#productStripInfo .h4ash3.ellipsis';

        const isProductFound = await page.waitForSelector(productSelector, { timeout: 3000 }).catch(() => false);

        if (!isProductFound) {
          console.warn(`No product found for barcode: ${barcode}`);
          continue;
        }
        const productName = await page.textContent(productSelectorName);
        console.log(`Product found: ${productName.trim()}`);

        await page.click(productSelector);
        console.log('Clicked on the product strip.');

        await page.waitForTimeout(1000);

        let img = null;
        try {
          await page.waitForSelector('.carousel-inner', { timeout: 5000 });

          img = await page.locator('div.item.active img[src]').getAttribute('src').catch(() => null);

          const imgCount = await page.locator('div.item.active img[src]').count();
          console.log(`Number of matching images: ${imgCount}`);

          if (!img) {
            img = await page.locator('div.item.active img.IsImgZoomEnabled').getAttribute('src').catch(() => null);
            console.warn('Fallback: No image found using the primary selector.');
          }
          console.log(`Image found: ${img || 'null'}`);
        } catch (err) {
          console.error('Error while fetching the image:', err);
        }


        console.log(`Image found: ${img || 'null'}`);

        const breadcrumbData = await page.evaluate(() => {
          const breadcrumbElements = document.querySelectorAll('.breadcrumb-container .breadcrumb-item .breadcrumb-link span');
          const breadcrumbs = Array.from(breadcrumbElements).map((el) => el.textContent.trim());

          const category = breadcrumbs[2] || null;
          const subCategory = breadcrumbs[3] || null;

          return { category, subCategory };
        });
        console.log('Category:', breadcrumbData.category);
        console.log('Subcategory:', breadcrumbData.subCategory);

        const newItem = new Item({
          name: productName.trim(),
          categoryId: 1,
          itemPic: img,
          category: breadcrumbData.category,
          subCategory: breadcrumbData.subCategory || '',
          barcode,
        });

        await newItem.save();
        console.log(`Item saved: ${newItem.name} (${barcode})`);
      } catch (err) {
        console.warn(`Error while processing barcode: ${barcode}`, err);
      }
    }
  } catch (err) {
    console.error('Error during scraping:', err);
  } finally {
    await browser.close();
    console.log('Browser closed.');
    updatePricesFromStores();
  }
}

module.exports = scrapeAndSaveItems;
