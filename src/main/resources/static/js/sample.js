window.onload = (event) => {
  addEventListener();
};

const addEventListener = () => {
  const targetElement = document.getElementById('target');
  for (const targetInputElement of targetElement.getElementsByTagName('input')) {
    targetInputElement.addEventListener("keydown", event => {
      if (event.key === "Enter") {
        updateFrag(event.target.id);
      }
    });
  }
  for (const targetSpanElement of targetElement.getElementsByTagName('span')) {
    targetSpanElement.addEventListener("click", event => {
      updateFrag(event.target.id);
    });
  }
}

const updateFrag = (id) => {
  const targetElement = document.getElementById(`target`)
  const body = { id: id }
  fetch('/updateFrag', {
    method: 'POST',
    body: JSON.stringify(body),
    headers: { "Content-Type": "application/json" },
  }).then((response) => {
    return response.text();
  }).then(body => {
    targetElement.innerHTML = body
    addEventListener()
  }).catch((error) => {
    console.log(error);
  });
}