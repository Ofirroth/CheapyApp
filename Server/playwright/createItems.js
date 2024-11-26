const { chromium } = require('playwright');
const Item = require('../models/item');
const Category = require('../models/category');
const SubCategory = require('../models/subCategory');
const Barcode = require('../models/barcode');
const updatePricesFromStores = require('./getPrices');

let categoryCounter = 1; // Counter for unique category IDs
let subCategoryCounter = 1; // Counter for unique subcategory IDs

/**
 * Main function to scrape items from a specific website and save them into the database.
 * This involves searching for items by barcodes, extracting product details,
 * and organizing data into categories and subcategories.
 */
async function scrapeAndSaveItems() {
  const browser = await chromium.launch({ headless: false }); // Launch browser in non-headless mode for debugging
  const page = await browser.newPage();

  try {
    const barcodes = await Barcode.find({}); // Fetch all barcodes from the database
    if (!barcodes.length) {
      return;
    }

    const url = 'https://shop.hazi-hinam.co.il/';
    await page.goto(url, { waitUntil: 'domcontentloaded' }); // Navigate to the website and wait for the DOM to load

    for (const barcodeDoc of barcodes) {
      const barcode = barcodeDoc.barcode; // Extract the barcode from the current document
      let categoryId = null;
      let subCategoryId = null;
      let activeCategoryImg = null;

      try {
        await page.getByRole('button', { name: 'חיפוש' }).click(); // Open search functionality
        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').fill(barcode); // Input the barcode
        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').press('Enter'); // Trigger search

        const productSelector = '#productStripInfo';
        const productSelectorName = '#productStripInfo .h4ash3.ellipsis';

        // Check if the product exists by waiting for a specific selector
        const isProductFound = await page.waitForSelector(productSelector, { timeout: 3000 }).catch(() => false);
        if (!isProductFound) {
          continue;
        }

        const productName = await page.textContent(productSelectorName); // Get the product name

        // Check if the item already exists
        const existingItem = await Item.findOne({ name: productName.trim() });
                if (existingItem) {
                  console.log(`Item with name "${productName.trim()}" already exists.`);
                  continue; // Skip creating this item
                }
        await page.click(productSelector); // Click the product to view its details
        await page.waitForTimeout(1000); // Allow time for the details page to load

        let img = null;
        try {
          // Extract the product image from the carousel, if available
          await page.waitForSelector('.carousel-inner', { timeout: 5000 });
          img = await page.locator('div.item.active img[src]').getAttribute('src').catch(() => null);

          // Fallback to an alternative image source if the first attempt fails
          if (!img) {
            img = await page.locator('div.item.active img.IsImgZoomEnabled').getAttribute('src').catch(() => null);
          }
        } catch (err) {
          // Image extraction error handled here
        }

        // Extract breadcrumb data for category and subcategory from the DOM
        const breadcrumbData = await page.evaluate(() => {
          const breadcrumbElements = document.querySelectorAll('.breadcrumb-container .breadcrumb-item .breadcrumb-link span');
          const breadcrumbs = Array.from(breadcrumbElements).map((el) => el.textContent.trim());

          return {
            category: breadcrumbs[2] || null, // Third breadcrumb is the category
            subCategory: breadcrumbs[3] || null, // Fourth breadcrumb is the subcategory
          };
        });

        let activeCategory = null;

        try {
          // Identify and handle the active category in the navigation
          const activeCategories = await page.locator('li.active');
          const activeCategoryElement = activeCategories.first();
          activeCategory = await activeCategoryElement.locator('span').textContent(); // Extract category name
          activeCategoryImg = await activeCategoryElement.locator('.image img').getAttribute('src'); // Extract category image

          // Check if the category exists in the database
          if (activeCategory) {
            const existingCategory = await Category.findOne({ name: activeCategory });
            if (!existingCategory) {
              // Create and save the category if it does not exist
              categoryId = categoryCounter++;
              const newCategory = new Category({
                id: categoryId,
                name: activeCategory,
                image: activeCategoryImg,
              });
              await newCategory.save();
            } else {
              categoryId = existingCategory.id;
            }
          }

          // Handle subcategory creation or retrieval
          if (breadcrumbData.subCategory) {
            let subCategory = await SubCategory.findOne({ name: breadcrumbData.subCategory });
            if (!subCategory) {
              // Create and save the subcategory if it does not exist
              subCategoryId = subCategoryCounter++;
              subCategory = new SubCategory({
                id: subCategoryId,
                name: breadcrumbData.subCategory,
                image: activeCategoryImg,
                parent: categoryId,
              });
              await subCategory.save();
            } else {
              subCategoryId = subCategory.id;
            }
          }

          // Create a new item document and save it
          const newItem = new Item({
            name: productName.trim(),
            categoryId: subCategoryId,
            itemPic: img,
            category: breadcrumbData.category,
            subCategory: breadcrumbData.subCategory || '',
            barcode,
          });

          await newItem.save();
        } catch (err) {
          // Handle errors related to category and subcategory processing
        }
      } catch (err) {
        // Handle errors specific to the current barcode's scraping process
      }
    }
  } catch (err) {
    // Catch and handle high-level errors during the scraping process
  } finally {
    await browser.close();
    updatePricesFromStores();
  }
}

module.exports = scrapeAndSaveItems;
