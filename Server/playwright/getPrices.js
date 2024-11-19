const { chromium } = require('playwright');
const Store = require('../models/store');
const Item = require('../models/item');
const Price = require('../models/price');

async function updatePricesFromStores() {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();

  try {
    const stores = await Store.find({});
    if (!stores.length) {
      return;
    }

    const items = await Item.find({});
    if (!items.length) {
      return;
    }

    const processedStoreNames = new Set();

    for (const store of stores) {
      if (processedStoreNames.has(store.name)) {
        continue;
      }
      processedStoreNames.add(store.name);

      await page.goto(store.url, { waitUntil: 'domcontentloaded' });

      if (store.url === "https://yochananof.co.il/") {

        try {
          const button = page.locator(
            'button.MuiButtonBase-root.MuiButton-root.MuiButton-outlined.MuiButton-outlinedPrimary.MuiButton-sizeMedium.MuiButton-outlinedSizeMedium.muirtl-12hcy0p'
          ).filter({ hasText: 'הזמנות מבצעי הרשת והזמנות פיקאפ' });

          await button.waitFor({ state: 'visible', timeout: 5000 });

          await button.click();
        } catch (error) {
          const fallbackButton = page.getByRole('button', { name: 'הזמנות מבצעי הרשת והזמנות פיקאפ' });
          const box = await fallbackButton.boundingBox();
          if (box) {
            await page.mouse.click(box.x + box.width / 2, box.y + box.height / 2);
          } else {
          }
        }
      }

      for (const item of items) {
        const barcode = item.barcode;
        //Item has no barcode skipping
        if (!barcode) {
          continue;
        }

        try {

          if (store.searchButtonSelector.role) {
            await page.getByRole(store.searchButtonSelector.role, { name: store.searchButtonSelector.name }).click();
          } else {
            await page.click(store.searchButtonSelector);
          }

          await page.fill(store.searchInputSelector, barcode);
          await page.press(store.searchInputSelector, 'Enter');

          if (store.name === "יוחננוף") {
            await page.click(store.searchButtonSelector);
            await page.waitForTimeout(3000);
          }

          try {
            const productStrip = page.locator(store.productStripSelector).nth(0);
            await productStrip.click();
          } catch (error) {
          }

          const priceText = null;
          try {
            const priceText = await page.textContent(store.priceSelector);
            const price = parseFloat(priceText.replace(/[^\d.]/g, ''));

            const existingPrice = await Price.findOne({ itemId: item._id, storeId: store._id });
            if (existingPrice) {
              existingPrice.price = price;
              await existingPrice.save();
            } else {
              const newPrice = new Price({
                itemId: item._id,
                price,
                storeId: store._id,
              });
              await newPrice.save();
            }

            const storesWithSameName = stores.filter(
              s => s.name === store.name && s._id.toString() !== store._id.toString()
            );
            for (const duplicateStore of storesWithSameName) {
              const duplicatePrice = await Price.findOne({ itemId: item._id, storeId: duplicateStore._id });
              if (!duplicatePrice) {
                const newDuplicatePrice = new Price({
                  itemId: item._id,
                  price,
                  storeId: duplicateStore._id,
                });
                await newDuplicatePrice.save();
              }
            }
          } catch (error) {
          }
          await page.click(store.closeButtonSelector);
        } catch (err) {
        }
      }
    }
  } catch (err) {
  } finally {
    await browser.close();
  }
}

module.exports = updatePricesFromStores;
