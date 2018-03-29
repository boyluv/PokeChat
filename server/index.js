const express = require('express')
const path = require('path')
const PORT = process.env.PORT || 5000

const app = express()

app.get('/',
(req, res) => {
    res.send("Hi me")
        })
        
app.listen(PORT, () => console.log('Example app listening on port 5000!'))  