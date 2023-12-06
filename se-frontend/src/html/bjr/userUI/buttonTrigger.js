/*----------------页面跳转-------------------*/
document.addEventListener('DOMContentLoaded', function() {
    var currentDisplayed = 'air'; // 初始显示 air

    function updateImages(clicked) {
        var airImg = document.querySelector('.image-air-button img');
        var billImg = document.querySelector('.image-bill-button img');

        if (clicked === 'air') {
            if (currentDisplayed !== 'air') {
                // 如果当前显示的不是 air，则更新图片
                airImg.src = './air.jpg';
                billImg.src = './bill.jpg';
                currentDisplayed = 'air';
            }
        } else if (clicked === 'bill') {
            if (currentDisplayed !== 'bill') {
                // 如果当前显示的不是 bill，则更新图片
                airImg.src = './air2.jpg';
                billImg.src = './bill2.jpg';
                currentDisplayed = 'bill';
            }
        }
    }

    // 为 air-button 添加点击事件
    document.querySelector('.image-air-button').addEventListener('click', function() {
        updateImages('air');
    });

    // 为 bill-button 添加点击事件
    document.querySelector('.image-bill-button').addEventListener('click', function() {
        updateImages('bill');
    });
});

/*-----------------------时钟-----------------------*/
function updateClock() {
    var now = new Date(); // 获取当前时间
    var year = now.getFullYear(); // 年份
    var month = now.getMonth() + 1; // 月份，从0开始所以加1
    var date = now.getDate(); // 日期
    var hours = now.getHours(); // 0-23
    var minutes = now.getMinutes(); // 0-59
    var seconds = now.getSeconds(); // 0-59

    // 格式化月份、日期、小时、分钟和秒钟，确保始终有两位数字
    month = month < 10 ? '0' + month : month;
    date = date < 10 ? '0' + date : date;
    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;

    // 将时间格式设置为 YYYY-MM-DD HH:MM:SS
    var timeString = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes + ':' + seconds;

    // 设置 clock div 的内容
    document.getElementById('clock').textContent = timeString;
}

// 每秒钟更新时间
setInterval(updateClock, 1000);

// 初始化时立即更新时间
updateClock();

/*-----------------------电源开关-----------------------*/

document.addEventListener('DOMContentLoaded', function() {
    var powerButton = document.querySelector('.power-button');
    var image1 = document.querySelector('.power-switch-hidden-image');
    var image2 = document.querySelector('.power-switch-hidden-image2');

    powerButton.addEventListener('click', function() {
        if (image1.style.display === 'none') {
            image1.style.display = 'block';
            image2.style.display = 'none';
        } else {
            image1.style.display = 'none';
            image2.style.display = 'block';
        }
    });
});
/*--------------------------展示设置的温度-----------------------------------------------------*/

document.addEventListener('DOMContentLoaded', function() {
    // 获取温度显示元素
    var temperatureDisplay = document.querySelector('.big_temperature-display');

    // 确保初始值为20
    temperatureDisplay.textContent = '20';

    // 减少温度的按钮事件监听
    document.getElementById('small-switch-image-trigger').addEventListener('click', function() {
        var currentTemperature = parseInt(temperatureDisplay.textContent, 10); // 将温度字符串转换为数字
        temperatureDisplay.textContent = (currentTemperature - 1) ; // 更新温度显示
    });

    // 增加温度的按钮事件监听
    document.getElementById('plus-switch-image-trigger').addEventListener('click', function() {
        var currentTemperature = parseInt(temperatureDisplay.textContent, 10); // 将温度字符串转换为数字
        temperatureDisplay.textContent = (currentTemperature + 1) ; // 更新温度显示
    });
});

/*-----------------------------------------------调节风速档位-------------------------------------*/
document.querySelector('.small-wind-trigger').addEventListener('click', function() {
    var display = document.querySelector('.wind-speed-display');
    if (display.textContent === '中档') {
        display.textContent = '低档';
    } else if (display.textContent === '高档') {
        display.textContent = '中档';
    }
});

document.querySelector('.plus-wind-trigger').addEventListener('click', function() {
    var display = document.querySelector('.wind-speed-display');
    if (display.textContent === '低档') {
        display.textContent = '中档';
    } else if (display.textContent === '中档') {
        display.textContent = '高档';
    }
});

/*-----------------------------------------模式按钮---------------------------------------------*/
document.addEventListener('DOMContentLoaded', function() {
    var lastClicked = null; // 用于记录最后点击的图片按钮

    // 更新所有图片，只有最后被点击的图片变为**2.jpg版本
    function updateImages() {
        var allButtons = document.querySelectorAll('.zhileng-button, .zhire-button, .songfeng-button, .auto-button');
        allButtons.forEach(function(button) {
            var img = button.querySelector('img');
            var src = img.getAttribute('src');
            img.src = src.replace('2.jpg', '.jpg'); // 将所有图片重置为初始状态
        });
        if (lastClicked) {
            var img = lastClicked.querySelector('img');
            var src = img.getAttribute('src');
            img.src = src.replace('.jpg', '2.jpg'); // 更新最后点击的图片
        }
    }

    // 为每个按钮添加点击事件
    document.querySelector('.zhileng-button').addEventListener('click', function() {
        lastClicked = this; // 更新最后点击的按钮
        updateImages(); // 更新所有图片
    });
    document.querySelector('.zhire-button').addEventListener('click', function() {
        lastClicked = this;
        updateImages();
    });
    document.querySelector('.songfeng-button').addEventListener('click', function() {
        lastClicked = this;
        updateImages();
    });
    document.querySelector('.auto-button').addEventListener('click', function() {
        lastClicked = this;
        updateImages();
    });
});
