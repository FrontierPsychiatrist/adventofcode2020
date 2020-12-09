const input = document.getElementById('input').textContent

const seatnumbers = input.split('\n')

function parseSeatNumber(v) {
  let currentRow = 127
  for (let i = 0; i < 7; i++) {
    if (v[i] === 'F') {
      currentRow -= Math.pow(2, 6 - i)
    }
  }

  let currentSeat = 7
  for (let i = 0; i < 3; i++) {
    if (v[7 + i] === 'L') {
      currentSeat -= Math.pow(2, 2 - i)
    }
  }
  return { row: currentRow, column: currentSeat }
}


function calculateSeatId(seat) {
  return seat.row * 8 + seat.column
}

const seats = seatnumbers.map(parseSeatNumber)
const seatIds = seats
  .map(calculateSeatId)

function findHighestSeatId() {
  let highestSeatId = 0

  seatIds.forEach((seatId, i) => {
    if (seatId > highestSeatId) {
      console.log(seatId, i)
      highestSeatId = seatId
    }
  })
}

seatIds.sort()
console.log(seatIds)
let mySeat
for (let i = 1; i < seatIds.length - 1; i++) {
  console.log(seatIds[i + 1] - seatIds[i])
  if (seatIds[i + 1] - seatIds[i] > 1) {
    mySeat = seatIds[i] + 1
    break
  }
}

console.log(mySeat)
