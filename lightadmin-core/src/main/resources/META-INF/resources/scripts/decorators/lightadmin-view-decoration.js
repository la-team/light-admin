/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function decorateUIControls(container) {
    $(".chzn-select", $(container)).chosen({allow_single_deselect: true});

    $("select, input:checkbox, input:radio, input:file", $(container)).uniform();

    $(".input-date", $(container)).mask("9999-99-99");

    $(".input-date", $(container)).datepicker({
        autoSize: true,
//        appendText: '(YYYY-MM-DD)',
        dateFormat: 'yy-mm-dd'
    });

    $( '.timepicker' ).timeEntry( {
        show24Hours: true, // 24 hours format
        showSeconds: true, // Show seconds?
        spinnerImage: ApplicationConfig.BASE_URL + 'images/ui/spinnerUpDown.png', // Arrows image
        spinnerSize: [17, 26, 0], // Image size
        spinnerIncDecOnly: true // Only up and down arrows
    } );
}

function formViewVisualDecoration(container) {
    decorateUIControls(container);

    $('.wysiwyg', $(container)).wysiwyg({
        iFrameClass: "wysiwyg-input",
        removeHeadings: true,
        controls: {
            bold: { visible: true },
            italic: { visible: true },
            underline: { visible: true },
            strikeThrough: { visible: false },

            justifyLeft: { visible: true },
            justifyCenter: { visible: true },
            justifyRight: { visible: true },
            justifyFull: { visible: true },

            indent: { visible: true },
            outdent: { visible: true },

            subscript: { visible: false },
            superscript: { visible: false },

            undo: { visible: true },
            redo: { visible: true },

            insertOrderedList: { visible: true },
            insertUnorderedList: { visible: true },
            insertHorizontalRule: { visible: false },

            h1: {
                visible: true,
                className: 'h1',
                command: 'formatBlock',
                arguments: '<h1>',
                tags: ['h1'],
                tooltip: 'Header 1'
            },
            h2: {
                visible: true,
                className: 'h2',
                command: 'formatBlock',
                arguments: '<h2>',
                tags: ['h2'],
                tooltip: 'Header 2'
            },
            h3: {
                visible: true,
                className: 'h3',
                command: 'formatBlock',
                arguments: '<h3>',
                tags: ['h3'],
                tooltip: 'Header 3'
            },
            h4: {
                visible: true,
                className: 'h4',
                command: 'formatBlock',
                arguments: '<h4>',
                tags: ['h4'],
                tooltip: 'Header 4'
            },
            h5: {
                visible: true,
                className: 'h5',
                command: 'formatBlock',
                arguments: '<h5>',
                tags: ['h5'],
                tooltip: 'Header 5'
            },
            h6: {
                visible: true,
                className: 'h6',
                command: 'formatBlock',
                arguments: '<h6>',
                tags: ['h6'],
                tooltip: 'Header 6'
            },

            cut: { visible: true },
            copy: { visible: true },
            paste: { visible: true },
            html: { visible: true },
            increaseFontSize: { visible: false },
            decreaseFontSize: { visible: false }
        }
    });
}
