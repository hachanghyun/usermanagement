
const express = require('express');
const app = express();
app.use(express.urlencoded({ extended: true }));
app.post('/sms', (req, res) => {
    const { phone } = req.query;
    const { message } = req.body;
    console.log(`[SMS] 📩 ${phone}: ${message}`);
    res.status(200).json({ result: "OK" });
});
app.listen(8082, () => console.log('🟢 SMS mock server listening on 8082'));
