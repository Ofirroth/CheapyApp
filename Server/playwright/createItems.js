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

        // Click the search button to activate the input field
        await page.getByRole('button', { name: 'חיפוש' }).click();
        console.log('Clicked the search button to activate the input field.');

        // Fill the search input and press Enter
        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').fill(barcode);
        console.log(`Filled search input with barcode: ${barcode}`);
        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').press('Enter');
        console.log('Pressed Enter to trigger the search.');

        const productSelector = '#productStripInfo';
        const productSelectorName = '#productStripInfo .h4ash3.ellipsis';

        // Wait for product results or handle no results
        const isProductFound = await page.waitForSelector(productSelector, { timeout: 3000 }).catch(() => false);

        if (!isProductFound) {
          console.warn(`No product found for barcode: ${barcode}`);
          continue; // Skip to the next barcode
        }
        const productName = await page.textContent(productSelectorName);
        console.log(`Product found: ${productName.trim()}`);

        // Click on the product strip
        await page.click(productSelector);
        console.log('Clicked on the product strip.');

        let img = await page.locator('#carousel3 img').getAttribute('src').catch(() => null);
        if (!img) {
          img = await page.locator('#carousel3 img').first().getAttribute('src').catch(() => null);
          console.warn('No image found.');
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

        // Save the item in the database
        const newItem = new Item({
          name: productName.trim(),
          categoryId: 1, // Replace with actual logic for category ID
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
