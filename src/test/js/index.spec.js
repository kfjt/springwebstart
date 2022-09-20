/**
 * @jest-environment jsdom
 */
const { hoge } = require('src/main/js/index')

it('Jest Can Assert', () => {
  expect(hoge(true)).toBe(true)
})
