const taskList = document.getElementById('taskList');
const taskText = document.getElementById('taskText');
const taskDate = document.getElementById('taskDate');
const searchBar = document.getElementById('searchBar');


function saveTasksToLocalStorage() {
    const tasks = [];
    document.querySelectorAll('#taskList .taskEl').forEach(taskEl => {
        const task = {
            text: taskEl.querySelector('.inputTaskText').textContent,
            date: taskEl.querySelector('.inputTaskDate').textContent
        };
        tasks.push(task);
    });
    localStorage.setItem('tasks', JSON.stringify(tasks));
    console.log("Tasks saved:", tasks);
}


function loadTasksFromLocalStorage() {
    const tasks = JSON.parse(localStorage.getItem('tasks')) || [];
    tasks.forEach(task => {
        addTaskToDOM(task.text, task.date);
    });
}


function addTaskToDOM(taskTextValue, taskDateValue) {
    let listEl = document.createElement('span');
    listEl.classList.add('taskEl');

    let text = document.createElement('span');
    text.classList.add('inputTaskText');
    text.textContent = taskTextValue;

    let date = document.createElement('span');
    date.classList.add('inputTaskDate');
    date.textContent = taskDateValue || 'No date';

    listEl.onclick = function () {
        let inputText = document.createElement('input');
        inputText.type = 'text';
        inputText.classList.add('inputTaskTextEdit');
        inputText.value = text.textContent;

        let inputDate = document.createElement('input');
        inputDate.type = 'date';
        inputDate.classList.add('inputTaskDateEdit');
        inputDate.value = (date.textContent === 'No date') ? '' : date.textContent;

        function saveChanges() {
            text.textContent = inputText.value;
            date.textContent = inputDate.value ? inputDate.value : 'No date';
            listEl.replaceChild(text, inputText);
            listEl.replaceChild(date, inputDate);
            listEl.style.backgroundColor = 'transparent';
            saveTasksToLocalStorage();
        }

        inputText.addEventListener('focusout', function () {
            setTimeout(() => {
                if (!document.activeElement.classList.contains('inputTaskDateEdit')) {
                    saveChanges();
                }
            }, 0);
        });

        inputDate.addEventListener('focusout', function () {
            setTimeout(() => {
                if (!document.activeElement.classList.contains('inputTaskTextEdit')) {
                    saveChanges();
                }
            }, 0);
        });

        listEl.replaceChild(inputText, text);
        listEl.replaceChild(inputDate, date);
        inputText.focus();
        listEl.style.backgroundColor = '#ddd';
    };

    let deleteButton = document.createElement('button');
    deleteButton.innerHTML = '🗑️';
    deleteButton.classList.add('deleteButton');
    deleteButton.onclick = function() {
        listEl.remove();
        saveTasksToLocalStorage();
    };

    listEl.appendChild(text);
    listEl.appendChild(date);
    listEl.appendChild(deleteButton);
    taskList.appendChild(listEl);
}



function addTask() {
    if (taskText.value.length < 3 || taskText.value.length > 255) {
        alert('Task must be between 3 and 255 characters');
    } else if (new Date(taskDate.value) < new Date()) {
        alert('Please select a future date');
    } else {
        addTaskToDOM(taskText.value, taskDate.value);
        saveTasksToLocalStorage();
        taskText.value = '';
        taskDate.value = '';
    }
}


function searchTask() {
    let searchText = searchBar.value.toLowerCase();
    let tasks = document.querySelectorAll('#taskList .taskEl');

    if (searchText.length >= 2) {
        tasks.forEach(taskEl => {
            let taskTextEl = taskEl.querySelector('.inputTaskText');
            let taskText = taskTextEl.textContent.toLowerCase();

            if (taskText.includes(searchText)) {
                taskEl.style.display = '';
                taskTextEl.innerHTML = taskTextEl.textContent.replace(
                    new RegExp(`(${searchText})`, "gi"),
                    match => `<mark>${match}</mark>`
                );
            } else {
                taskEl.style.display = 'none';
            }
        });
    } else {
        tasks.forEach(taskEl => {
            taskEl.style.display = '';
            let taskTextEl = taskEl.querySelector('.inputTaskText');
            taskTextEl.innerHTML = taskTextEl.textContent;
        });
    }
}

document.getElementById('addTaskButton').addEventListener('click', addTask);
searchBar.addEventListener('input', searchTask);

loadTasksFromLocalStorage();
