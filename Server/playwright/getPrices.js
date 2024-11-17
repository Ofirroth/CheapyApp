const { chromium } = require('playwright');
const Store = require('../models/store');
const Item = require('../models/item');
const Price = require('../models/price');

async function updatePricesFromStores() {
  const browser = await chromium.launch({ headless: false, slowMo: 100 });
  const page = await browser.newPage();

  try {
    console.log('Fetching store configurations...');
    const stores = await Store.find({});
    if (!stores.length) {
      console.log('No stores found.');
      return;
    }

    console.log('Fetching items...');
    const items = await Item.find({});
    if (!items.length) {
      console.log('No items found.');
      return;
    }

    for (const store of stores) {
      console.log(`Processing store: ${store.name} (${store.url})`);

      // Navigate to store URL
      await page.goto(store.url, { waitUntil: 'domcontentloaded' });

      for (const item of items) {
        const barcode = item.barcode;
        if (!barcode) {
          console.warn(`Item ${item.name} has no barcode, skipping.`);
          continue;
        }

        try {
          console.log(`Searching for item: ${item.name} (Barcode: ${barcode})`);

         if (store.searchButtonSelector.role) {
             await page.getByRole(store.searchButtonSelector.role, { name: store.searchButtonSelector.name }).click();
         } else {
             await page.click(store.searchButtonSelector);
         }

          // Fill search input
          await page.fill(store.searchInputSelector, barcode);

          // Trigger search (Enter key)
          await page.press(store.searchInputSelector, 'Enter');

          // Wait for the product strip
          const isProductFound = await page.waitForSelector(store.productStripSelector, { timeout: 5000 }).catch(() => false);

          if (!isProductFound) {
            console.warn(`No product found for item: ${item.name}`);
            continue;
          }
          console.warn(`product found: ${item.name}`);
          await page.click(store.productStripSelector);
          console.log('Clicked on the product strip.');

          // Extract price
          const priceText = await page.textContent(store.priceSelector).catch(() => null);
          if (!priceText) {
            console.warn(`Price not found for item: ${item.name}`);
            continue;
          }

          const price = parseFloat(priceText.replace(/[^\d.]/g, ''));
          console.log(`Price for ${item.name} at ${store.name}: ${price}`);

          // Check and update/save price in the database
          const existingPrice = await Price.findOne({ itemId: item._id, storeId: store._id });
          if (existingPrice) {
            existingPrice.price = price;
            await existingPrice.save();
            console.log(`Updated price for ${item.name} at ${store.name}`);
          } else {
            const newPrice = new Price({
              itemId: item._id,
              price,
              storeId: store._id,
            });
            await newPrice.save();
            console.log(`Saved price for ${item.name} at ${store.name}`);
          }
        } catch (err) {
          console.warn(`Error processing item: ${item.name} at ${store.name}`, err);
        }
      }
    }
  } catch (err) {
    console.error('Error during scraping:', err);
  } finally {
    await browser.close();
    console.log('Browser closed.');
  }
}

module.exports = updatePricesFromStores;
