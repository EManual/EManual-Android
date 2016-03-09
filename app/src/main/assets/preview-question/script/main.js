(function(window, $) {
  $(document).ready(function() {
    var selector_description = $('#question_description')
    var selector_option_container = $('#option-container')
    var select_tpl_option = $('#tpl_option')
    var select_question_answer = $('#question_answer')
    /**
     * 渲染题目
     */
    window.updateQuestion = function(question) {
      //渲染描述
      selector_description.html(question.description)

      if (question.type === 'choice') {
        var options = []
        question.options.forEach(function(option, index) {
          options.push(select_tpl_option.html().replace(/{{option_id}}/g, 'option_' + index).replace(/{{option_description}}/g, option))
        })
        selector_option_container.html(options.join(''))
      }
      //渲染答案
      question.answer = question.answer || '答案: 无'
      select_question_answer.html(question.answer)
    }
    /**
     * @return array 已选择的东西列表
     */
    window.getUserAnswer = function() {
      var checkeds = []
      $('#option-container input').forEach(function(input, index) {
        if ($(input)[0].checked) {
          checkeds.push(index)
        }
      })
      return checkeds
    }
  })
})(window, Zepto);
