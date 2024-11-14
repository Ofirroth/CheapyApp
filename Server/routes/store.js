const express = require('express');
const router = express.Router();
const storeController = require('../controllers/store');

router.get('/', storeController.getStores);
router.post('/getTotalPriceByStore', storeController.getTotalPriceByStore);
router.get('/:city', storeController.getStoreByCity);


module.exports = router;