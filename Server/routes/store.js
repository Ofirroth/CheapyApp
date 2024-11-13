const express = require('express');
const router = express.Router();
const storeController = require('../controllers/store');

router.get('/', storeController.getStores);
router.get('/total-price/:storeName', storeController.getTotalPriceByStore);

module.exports = router;