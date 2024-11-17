//const fs = require('fs');
//const path = require('path');
//const axios = require('axios');
//const xlsx = require('xlsx');
//const Barcode = require('./models/barcode');
//const scrapeStore = require('./playwright/scraper');
//
//const xlsxUrl = 'https://www.gov.il/BlobFolder/policy/economy_dgi_instructions_04_53/he/instructions_4-53_appendix01inst4-53.xlsx';
//
//const localFilePath = path.join(__dirname, 'data.xlsx');
//const outputFilePath = path.join(__dirname, 'sampled_products_with_status.json');
//
//async function downloadAndProcessXLSX() {
//  try {
//    console.log('Downloading XLSX file...');
//    const response = await axios({
//      url: xlsxUrl,
//      method: 'GET',
//      responseType: 'stream',
//    });
//
//    const writer = fs.createWriteStream(localFilePath);
//    response.data.pipe(writer);
//
//    await new Promise((resolve, reject) => {
//      writer.on('finish', resolve);
//      writer.on('error', reject);
//    });
//
//    console.log('XLSX file downloaded.');
//
//    const workbook = xlsx.readFile(localFilePath);
//    const sheetName = workbook.SheetNames[0];
//    const sheet = workbook.Sheets[sheetName];
//
//    const data = xlsx.utils.sheet_to_json(sheet, { defval: '' });
//
//    console.log('Sampling data: selecting 1 barcode every 60 rows...');
//    const sampledData = data.filter((_, index) => index % 60 === 0).slice(0, 100);
//
//    console.log(`Selected ${sampledData.length} barcodes for processing.`);
//
//    // Extract barcodes and names for processing
//    const barcodes = sampledData.map((row) => ({
//      barcode: String(row['ברקוד']),
//      productName: row['שם מוצר'],
//    }));
//
//    console.log('Sending barcodes to scrapeStore...');
//    const results = await scrapeStore(barcodes);
//
//    // Update the sampled data with status based on scrapeStore results
//    sampledData.forEach((row) => {
//      const result = results.find((r) => r.barcode === String(row['ברקוד']));
//      row.status = result ? 1 : 0; // 1 if found, 0 if not found
//    });
//
//    // Write the updated sampled data to a JSON file
//    fs.writeFileSync(outputFilePath, JSON.stringify(sampledData, null, 2), 'utf8');
//    console.log(`Sampled products with status saved to: ${outputFilePath}`);
//  } catch (error) {
//    console.error('Error downloading or processing XLSX file:', error);
//  }
//}
//
//module.exports = downloadAndProcessXLSX;
