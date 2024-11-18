const { chromium } = require('playwright');
const Store = require('../models/store');
const Item = require('../models/item');
const Price = require('../models/price');

async function updatePricesFromStores() {
  const browser = await chromium.launch({ headless: false });
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

          await page.fill(store.searchInputSelector, barcode);
          await page.press(store.searchInputSelector, 'Enter');

          console.warn(`Product found: ${item.name}`);
          await page.click(store.productStripSelector);
          console.log('Clicked on the product strip.');

          const priceText = await page.textContent(store.priceSelector).catch(() => null);
          if (!priceText) {
            console.warn(`Price not found for item: ${item.name}`);
            continue;
          }

          const price = parseFloat(priceText.replace(/[^\d.]/g, ''));
          console.log(`Price for ${item.name} at ${store.name}: ${price}`);

          await page.click(store.closeButtonSelector);

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
