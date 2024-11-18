const express = require('express');
const { searchBarcode } = require('../controllers/barcode');

const router = express.Router();

router.get('/search/:barcode', searchBarcode);

module.exports = router;
