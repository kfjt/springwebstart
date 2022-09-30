exports.font = (async () =>
({
  gothic: {
    data: await fetch('/fonts/ipaexg.ttf').then((res) => res.arrayBuffer()),
    fallback: true,
  },
  mincho: {
    data: await fetch('/fonts/ipaexm.ttf').then((res) => res.arrayBuffer()),
  },
})
)()
