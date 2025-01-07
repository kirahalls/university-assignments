import { $ } from "/static/jquery/src/jquery.js";

function say_hi(elt) {
    console.log("Welcome to", elt.text());
}

function make_table_sortable(table) {
    let comparison_order = "";
    let selected_index;
    $(".sort-column").on("click", (e) => {
        selected_index = $(e.target).index();
        let non_selected_index = selected_index === 2 ? 1 : 2;
        let non_selected_column = ($("th")).get(non_selected_index);
        if (non_selected_column) {
            $(non_selected_column).removeClass("sort-asc sort-desc");
            $(non_selected_column).removeAttr("aria-sort");
        }
        
        if ($(e.target).hasClass("sort-asc")) {
            $(e.target).removeClass("sort-asc");
            $(e.target).addClass("sort-desc");
            comparison_order = "sort-desc";
            $(e.target).attr("aria-sort","descending");
        }
        else if ($(e.target).hasClass("sort-desc")) {
            $(e.target).removeClass("sort-desc");
            comparison_order = "";
            $(e.target).removeAttr("aria-sort");

        }
        else {
            $(e.target).addClass("sort-asc");
            comparison_order = "sort-asc";
            $(e.target).attr("aria-sort","ascending");
        }

        let row_array = $(table).find("tbody tr").toArray();
        row_array.sort((a, b) => {
            // Get last td elements in row a and b
            let td_a = $(a).find($("td")).get(selected_index);
            let td_b = $(b).find($("td")).get(selected_index);

            // Convert text elements of td a and td b into a number
            let num_a = parseFloat($(td_a).data("value"));
            let num_b = parseFloat($(td_b).data("value"));

            // If sort asc
            if (comparison_order === "sort-asc") {
                if (num_a < num_b) {
                    return -1;
                }
                else if (num_a > num_b) {
                    return 1;
                }
                return 0;
            }
            // If sort desc
            else if (comparison_order === "sort-desc") {
                if (num_a > num_b) {
                    return -1;
                }
                else if (num_a < num_b) {
                    return 1;
                }
                return 0;
            }
            else {
                if ($(a).data("index") < $(b).data("index")) {
                    return -1;
                }
                else if ($(td_a).data("index") > $(td_b).data("index")) {
                    return 1;
                }
                return 0;
            }
        });
        $(row_array).appendTo($($(table).find("tbody")));

    });



}

function make_form_async(form) {
    $(form).on("submit", function(event) {
        event.preventDefault();
        const formData = new FormData($(form)[0]);
        $.ajax({
            url: $(form).attr("action"),
            data: formData,
            type: 'POST',
            processData: false, 
            contentType: false,
            mimeType: $(form).attr("enctype"),
            success: function(data) {
                $(form).replaceWith("<p> Upload successful </p>");

            }, 
            error: function(data) {
                console.log("Upload failed");
                $(form).find("button").removeAttr("disabled");
                $(form).find("input").removeAttr("disabled");
            }
        })
        $(form).find("button").attr("disabled");
        $(form).find("input").attr("disabled");
    });
}

function make_grade_hypothesized(table) {
    let current_state = "regular";
    let hypoth_button = $("<button> Hypothesize </button>");
    let final_grade_cell = $(table).find("tfoot tr td:last");
    $(hypoth_button).on("click", function(e) {
        if (current_state === "hypothesized") {
            $(table).removeClass("hypothesized");
            $(e.target).text("Hypothesize");
            current_state = "regular";

            // Remove inputs and restore original text
            let row_array = $(table).find("tbody tr").toArray();
            for (let row of row_array) {
                let current_cell = $(row).find("td:last");
                if ($(current_cell).hasClass("hypoth_grade")) {
                    console.log("replacing hypoth_grade class cell");
                    let new_cell = $("<td class='numbered_column'> </td>")
                    $(new_cell).text($(current_cell).data("old_val"));
                    $(new_cell).data("value", $(current_cell).data("old_val"));
                    $(new_cell).data("weight", $(current_cell).data("weight"));
                    $(current_cell).replaceWith($(new_cell));
                }
                continue;
                
                
            }
            console.log(" was hypothesis");
            $(final_grade_cell).data("value", compute_grades($(table)));
            $(final_grade_cell).text($(final_grade_cell).data("value"));
            
        }
        else if (current_state === "regular") {
            $(table).addClass("hypothesized");
            $(e.target).text("Actual grades");
            current_state = "hypothesized";

            // Replace all "Not Due" or "Ungraded" td elements with num inputs
            let row_array = $(table).find("tbody tr").toArray();
            for (let row of $(row_array)) {
                let current_cell = $(row).find("td:last");
                if ($(current_cell).data("value") === "Ungraded" ) {
                    let new_cell = $("<td class='numbered_column hypoth_grade'> <input class='hypoth_grade' type='number'> </td>")
                    $(new_cell).data("old_val", "Ungraded");
                    $(new_cell).data("weight", $(current_cell).data("weight"));
                    $(current_cell).replaceWith($(new_cell));
                }
                else if ($(current_cell).data("value") === "Not Due"){
                    let new_cell = $("<td class='numbered_column hypoth_grade'> <input class='hypoth_grade' type='number'> </td>")
                    $(new_cell).data("old_val", "Not Due");
                    $(new_cell).data("weight", $(current_cell).data("weight"));
                    $(current_cell).replaceWith($(new_cell));
                }
                continue;
                
            }
            console.log("regular");
            $(final_grade_cell).data("value", compute_grades($(table)));
            $(final_grade_cell).text($(final_grade_cell).data("value"));
        }
    });
    $(table).on("keyup", function() {
        console.log("keyup function");
        $(final_grade_cell).data("value", compute_grades($(table)));
        $(final_grade_cell).text($(final_grade_cell).data("value"));
    });
    $(table).before(hypoth_button);

}

function compute_grades(table) {
    let is_hypothesized = $(table).hasClass("hypothesized");
    let row_array = $(table).find("tbody tr").toArray();
    let available_grade_points = 0.0;
    let earned_grade_points = 0.0;
    let final_grade;
    let current_cell;

    if (is_hypothesized) {
        for (let row of $(row_array)) {
            current_cell = $(row).find("td:last");
            
            if ($(current_cell).hasClass("hypoth_grade")) {
                let current_input = $(current_cell).find("input");
                if ($(current_input).val() !== "") {
                    let input_grade = parseFloat($(current_input).val());
                    available_grade_points += parseFloat($(current_cell).data("weight"));
                    earned_grade_points += parseFloat(input_grade / 100.0) * parseFloat($(current_cell).data("weight"));
                    continue;
                }
                else {

                    continue;
                }
            }
            else {
                
                if ($(current_cell).data("value") === "Missing") {
                    available_grade_points += parseFloat($(current_cell).data("weight"));
                    continue;
                }
                else {
                    available_grade_points += parseFloat($(current_cell).data("weight"));
                    earned_grade_points += (parseFloat($(current_cell).text()) / 100.0) * parseFloat($(current_cell).data("weight"));
                    continue;
                }
                
            }
            
            
        }
        final_grade = Math.round((earned_grade_points / available_grade_points) * 100.0, 2);
    }
    else 
    {
        for (let row of $(row_array)) {
            current_cell = $(row).find("td:last");
            if ($(current_cell).data("value") === "Ungraded") {
                continue;
            }
            else if ($(current_cell).data("value") === "Not Due") {
                continue;
            }
            else if ($(current_cell).data("value") === "Missing") {
                available_grade_points += parseFloat($(current_cell).data("weight"));
                continue;
            }
            else {
                available_grade_points += parseFloat($(current_cell).data("weight"));
                earned_grade_points += (parseFloat($(current_cell).data("value")) / 100) * parseFloat($(current_cell).data("weight"));
                continue;
            }
        }
        final_grade = Math.round((earned_grade_points / available_grade_points) * 100, 2);
    }
    return final_grade;
}

say_hi($("h1"));
make_table_sortable($(".sortable"));
make_form_async($(".not-due-assign-form"));
make_grade_hypothesized($(".hypothesizable"));