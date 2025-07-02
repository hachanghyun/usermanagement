const express = require('express');
const app = express();

app.use(express.json());

let kakaoCount = 0;

app.post('/kakaotalk-messages', (req, res) => {
    const { phone, message } = req.body;
    kakaoCount++;
    console.log(`[KAKAO #${kakaoCount}] 📩 ${phone}: ${message}`);
    res.sendStatus(200);
});

app.listen(8081, () => console.log('🟢 Kakao mock server listening on 8081'));