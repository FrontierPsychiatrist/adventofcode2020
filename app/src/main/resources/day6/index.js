const MAX_ITER = 100000;
let iteration = 0;

function getInput() {
  return fetch('input').then(res => res.text())
}

function getGroups(input) {
  let currentIndex = 0
  const groups = []
  let currentGroup = []
  while (iteration < MAX_ITER) {
    const newIndex = input.indexOf('\n', currentIndex)
    if (newIndex === -1) {
      groups.push(currentGroup)
      break
    }
    const line = input.substring(currentIndex, newIndex)
    if (line === '') {
      groups.push(currentGroup)
      currentGroup = []
    } else {
      currentGroup.push(line)
    }
    currentIndex = newIndex + 1
    iteration++
  }
  return groups;
}

function part1(groups) {
  let count = 0
  for (group of groups) {
    const yes = new Set()
    for (p of group) {
      for (q of p) {
        yes.add(q)
      }
    }
    count += yes.size
  }
  return count
}

function part2(groups) {
  let count = 0
  for (group of groups) {
    const yes = []
    for (p of group) {
      for (q of p) {
        const code = q.charCodeAt(0)
        if (yes[code] === undefined) {
          yes[code] = 1
        } else {
          yes[code]++
        }
      }
    }
    const size = yes.filter(y => y === group.length).length
    count += size
  }
  return count
}


async function run() {
  const input = await getInput();
  const groups = getGroups(input)

  console.log(part2(groups))
}

run()
.catch(console.error)

