const path = require('path');
const glob = require('glob');

const srcDir = './src/main/js';
const entries = Object.fromEntries(
  glob
    .sync('**/*.js', { cwd: srcDir })
    .map((key) => [
      path.join(
        path.dirname(key),
        `${path.basename(key, '.js')}.bundle.js`
      ),
      path.resolve(srcDir, key)
    ])
);

module.exports = {
  // entry: { ...entries, shared: ['@pdfme/ui', '@pdfme/common'] },
  entry: entries,
  output: {
    path: path.resolve(__dirname, 'src/main/resources/static/js'),
    // filename: '[name].bundle.js',
    filename: '[name]',
    library: { type: 'umd' }
  },
};
