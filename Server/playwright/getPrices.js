const { chromium } = require('playwright');
const Store = require('../models/store');
const Item = require('../models/item');
const Price = require('../models/price');

/**
 * Function to update item prices from various stores by scraping their websites.
 * Iterates through all stores and items, scrapes the latest prices, and updates the database.
 */
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

    const processedStoreNames = new Set(); // Track stores already processed by name to avoid duplicates

    for (const store of stores) {
      if (processedStoreNames.has(store.name)) {
        continue;
      }
      processedStoreNames.add(store.name);

      await page.goto(store.url, { waitUntil: 'domcontentloaded' }); // Navigate to the store's website

      // Handle specific logic for "יוחננוף" (Yochananof) store
      if (store.url === "https://yochananof.co.il/") {
        try {
          // Attempt to click the specific button for offers and pickup orders
          const button = page.locator(
            'button.MuiButtonBase-root.MuiButton-root.MuiButton-outlined.MuiButton-outlinedPrimary.MuiButton-sizeMedium.MuiButton-outlinedSizeMedium.muirtl-12hcy0p'
          ).filter({ hasText: 'הזמנות מבצעי הרשת והזמנות פיקאפ' });
          await button.waitFor({ state: 'visible', timeout: 5000 });
          await button.click();
        } catch (error) {
          // Fallback if the button is not clickable via the primary method
          const fallbackButton = page.getByRole('button', { name: 'הזמנות מבצעי הרשת והזמנות פיקאפ' });
          const box = await fallbackButton.boundingBox();
          if (box) {
            await page.mouse.click(box.x + box.width / 2, box.y + box.height / 2); // Simulate mouse click
          }
        }
      }

      for (const item of items) {
        const barcode = item.barcode;
        if (!barcode) {
          continue;
        }

        try {
          // Handle search button logic based on the store configuration
          if (store.searchButtonSelector.role) {
            await page.getByRole(store.searchButtonSelector.role, { name: store.searchButtonSelector.name }).click();
          } else {
            await page.click(store.searchButtonSelector);
          }

          // Fill the search input with the item's barcode and trigger the search
          await page.fill(store.searchInputSelector, barcode);
          await page.press(store.searchInputSelector, 'Enter');

          // Special handling for the "יוחננוף" store
          if (store.name === "יוחננוף") {
            await page.click(store.searchButtonSelector);
            await page.waitForTimeout(3000); // Wait for the search results to load
          }

          try {
            // Locate and click the first product in the search results
            const productStrip = page.locator(store.productStripSelector).nth(0);
            await productStrip.click();
          } catch (error) {
            // Handle case where product selection fails
          }

          try {
            // Extract the product's price from the specified selector
            const priceText = await page.textContent(store.priceSelector);
            const price = parseFloat(priceText.replace(/[^\d.]/g, '')); // Parse numeric value from the price string

            // Check if the price already exists for the item in this store
            const existingPrice = await Price.findOne({ itemId: item._id, storeId: store._id });
            if (existingPrice) {
              existingPrice.price = price;
              await existingPrice.save();
            } else {
              // Create and save a new price entry
              const newPrice = new Price({
                itemId: item._id,
                price,
                storeId: store._id,
              });
              await newPrice.save();
            }

            // Handle duplicate stores with the same name but different IDs
            const storesWithSameName = stores.filter(
              s => s.name === store.name && s._id.toString() !== store._id.toString()
            );
            for (const duplicateStore of storesWithSameName) {
              const duplicatePrice = await Price.findOne({ itemId: item._id, storeId: duplicateStore._id });
              if (!duplicatePrice) {
                // Save the price for the duplicate store if it does not already exist
                const newDuplicatePrice = new Price({
                  itemId: item._id,
                  price,
                  storeId: duplicateStore._id,
                });
                await newDuplicatePrice.save();
              }
            }
          } catch (error) {
            // Handle errors related to price extraction
          }

          await page.click(store.closeButtonSelector);
        } catch (err) {
          // Handle errors specific to the current item and store
        }
      }
    }
  } catch (err) {
    // Handle high-level errors during the price update process
  } finally {
    await browser.close();
  }
}

module.exports = updatePricesFromStores;
