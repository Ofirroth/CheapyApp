const Barcode = require('../models/barcode');
const { scrapeStore } = require('../playwright/createItems');

async function findBarcode(barcode) {
  let record = await Barcode.findOne({ barcode });

  if (!record) {
    const storeData = await scrapeStore(barcode);

    record = new Barcode({
      barcode,
      productName: storeData.productName,
      storeData: storeData,
    });

    await record.save();
  }

  return record;
}

module.exports = { findBarcode };
