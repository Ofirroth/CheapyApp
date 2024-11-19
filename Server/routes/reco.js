const express = require('express');
const router = express.Router();
const recoController = require('../controllers/reco');

router.get('/:username', recoController.getRecommended);

module.exports = router;