const { chromium } = require('playwright');
const Item = require('../models/item');
const Category = require('../models/category');
const SubCategory = require('../models/subCategory');
const Barcode = require('../models/barcode');
const updatePricesFromStores = require('./getPrices');

let categoryCounter = 1;
let subCategoryCounter = 1;

async function scrapeAndSaveItems() {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();

  try {
    const barcodes = await Barcode.find({});
    if (!barcodes.length) {
      return;
    }

    const url = 'https://shop.hazi-hinam.co.il/';
    await page.goto(url, { waitUntil: 'domcontentloaded' });

    for (const barcodeDoc of barcodes) {
      const barcode = barcodeDoc.barcode;
      let categoryId = null;
      let subCategoryId = null;
      let activeCategoryImg = null;
      try {
        await page.getByRole('button', { name: 'חיפוש' }).click();

        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').fill(barcode);
        await page.getByPlaceholder('מה אפשר למצוא בשבילך ?').press('Enter');

        const productSelector = '#productStripInfo';
        const productSelectorName = '#productStripInfo .h4ash3.ellipsis';

        const isProductFound = await page.waitForSelector(productSelector, { timeout: 3000 }).catch(() => false);

        if (!isProductFound) {
          continue;
        }
        const productName = await page.textContent(productSelectorName);

        await page.click(productSelector);

        await page.waitForTimeout(1000);

        let img = null;
        try {
          await page.waitForSelector('.carousel-inner', { timeout: 5000 });

          img = await page.locator('div.item.active img[src]').getAttribute('src').catch(() => null);

          const imgCount = await page.locator('div.item.active img[src]').count();

          if (!img) {
            img = await page.locator('div.item.active img.IsImgZoomEnabled').getAttribute('src').catch(() => null);
          }
        } catch (err) {
        }

        await page.waitForTimeout(5000);
        const breadcrumbData = await page.evaluate(() => {

          const breadcrumbElements = document.querySelectorAll('.breadcrumb-container .breadcrumb-item .breadcrumb-link span');
          const breadcrumbs = Array.from(breadcrumbElements).map((el) => el.textContent.trim());

          const category = breadcrumbs[2] || null;
          const subCategory = breadcrumbs[3] || null;

          return { category, subCategory };
        });

       let activeCategory = null;

       try {

                  const activeCategories = await page.locator('li.active');
                  const activeCategoryCount = await activeCategories.count();

                  const activeCategoryElement = activeCategories.first();
                  const activeCategory = await activeCategoryElement.locator('span').textContent();
                  const activeCategoryImg = await activeCategoryElement.locator('.image img').getAttribute('src');


          if (activeCategory) {
            const existingCategory = await Category.findOne({ name: activeCategory });
            if (!existingCategory) {
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

          if (breadcrumbData.subCategory) {
                    let subCategory = await SubCategory.findOne({ name: breadcrumbData.subCategory });
                    if (!subCategory) {
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
                        }

        } catch (err) {
        }


    }
  } catch (err) {
  } finally {
    await browser.close();
    updatePricesFromStores();
  }
}

module.exports = scrapeAndSaveItems;
