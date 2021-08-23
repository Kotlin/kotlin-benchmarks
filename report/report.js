/**
 * Created by Mikhail.Glukhikh on 06/03/2015.
 *
 * Common functions for report building
 */

function sharedStart(array) {
    var A = array.slice(0).sort(),
        word1 = A[0], word2 = A[A.length - 1],
        L = word1.length, i = 0;
    while (i < L && word1.charAt(i) === word2.charAt(i)) i++;
    return word1.substring(0, i);
}

function fixed(value) {
    if (typeof value == 'number')
        return Number(value.toFixed(2));
    else if (typeof value == 'string')
        return Number(parseFloat(value).toFixed(2));
    else
        return "N/A"
}

function fieldValueOr1(object, field) {
    if (object) 
        return object[field]
    else
        return 1
}

function toErrorString(scoreError, score) {
    return "± " + (scoreError/score * 100).toFixed(2) +"%" + " (" + scoreError.toFixed(2) + ")";
}

function processTable(baseline, data, noMessage) {

    var baselineMap = {};
    for (var i = 0, len = baseline.length; i < len; i++) {
        //var key = baseline[i].benchmark + baseline[i].params.size;
        baselineMap[baseline[i].benchmark + fieldValueOr1(baseline[i].params, "size")] = baseline[i]
    }

    var names = [];
    for (i = 0, len = data.length; i < len; i++)
        names.push(data[i].benchmark)
    var commonPrefix = sharedStart(names);

    for (i = 0, len = data.length; i < len; i++) {        
        var key = data[i].benchmark + fieldValueOr1(data[i].params, "size");
        var score = data[i].primaryMetric.score;

        data[i].benchmark = data[i].benchmark.substring(commonPrefix.length);
        data[i].size = fixed(fieldValueOr1(data[i].params, "size"));

        data[i].score = fixed(score);
        data[i].unit = data[i].primaryMetric.scoreUnit;
        data[i].scoreString = score.toFixed(2);

        var scoreError = data[i].primaryMetric.scoreError;
        data[i].error = scoreError;
        data[i].errorString = toErrorString(scoreError, score);

        var baselineItem = baselineMap[key];
        if (baselineItem !== undefined) {
            var baselineScore = baselineItem.primaryMetric.score;
            var baselineError = baselineItem.primaryMetric.scoreError;
            data[i].baselineScore = baselineScore
            data[i].baselineScoreString = baselineScore.toFixed(2);
            data[i].baselineError = baselineError;
            data[i].baselineErrorString = toErrorString(baselineError, baselineScore);
            var diff = score - baselineScore;
            data[i].diff = fixed(diff);
            // Significant difference is chosen to be 2% of base score
            // or measurement error, whichever is higher
            var epsilon = 0.02 * baselineScore;
            var defeat = 0.5 * baselineScore;
            var limit = scoreError > epsilon ? scoreError : epsilon;
            if (diff < -limit) {
                data[i].diffString = diff.toFixed(2);
                data[i].diffPercent = diff / baselineScore * 100;
                data[i].diffPercentString = data[i].diffPercent.toFixed(2) + "%";
                data[i].diffString_class = "improvement";
                data[i].diffPercentString_class = "improvement";
            } else if (diff > limit) {
                data[i].diffString = "+" + diff.toFixed(2);
                data[i].diffPercent = diff / baselineScore * 100;
                data[i].diffPercentString = "+" + data[i].diffPercent.toFixed(2) + "%";
                data[i].diffString_class = (diff > defeat ? "defeat": "regression");
                data[i].diffPercentString_class = data[i].diffString_class;
            } else {
                data[i].diffString = "";
                data[i].diffPercent = 0;
                data[i].diffPercentString = "";
                data[i].diffString_class = "same";
                data[i].diffPercentString_class = "same";
            }
        } else {
            data[i].baselineScore = 0;
            data[i].baselineScoreString = "";
            data[i].baselineError = 0;
            data[i].baselineErrorString = "";
            data[i].diff = 0;
            data[i].diffString = noMessage;
            data[i].diffPercent = 0;
            data[i].diffPercentString = "";
            data[i].diffString_class = "regression";
            data[i].diffPercentString_class = "regression";
        }
    }

    var dynatable = $('#benchmarks').dynatable({
        features: {
            pushState: false,
            paginate: false,
            sort: true,
            recordCount: true

        }, dataset: {
            records: data,
            perPageDefault: 100,
            perPageOptions: [10, 20, 50, 100]
        }
    }).data('dynatable');
    dynatable.settings.dataset.originalRecords = data
    dynatable.process()

    dynatable.sorts.add('benchmark', 1);
    dynatable.sorts.add('size', 1);
    dynatable.records.sort();
    dynatable.dom.update();
}
