const express = require('express');
const port = 3000;

const server = express();

server.get('/', (req, res) => {
    res.send('Sample root page');
});

server.listen(port, () => {
    console.debug(`Server is listening on http://localhost:${port}`);
});