
const express = require('express');
const app = express();
app.use(express.json());
app.post('/kakaotalk-messages', (req, res) => {
    const { phone, message } = req.body;
    console.log(`[KAKAO] 📩 ${phone}: ${message}`);
    res.sendStatus(200);
});
app.listen(8081, () => console.log('🟢 Kakao mock server listening on 8081'));
