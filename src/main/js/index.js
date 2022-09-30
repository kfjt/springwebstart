const { generate } = require('@pdfme/generator')
const { Designer } = require('@pdfme/ui')

exports.Designer = Designer

module.exports.generate = generate
module.exports.hoge = (a) => a
