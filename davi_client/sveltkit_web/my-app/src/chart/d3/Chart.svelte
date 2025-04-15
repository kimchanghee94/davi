<script>
    import { onMount } from "svelte";

    let element;

    onMount(() => {
        // >>> hello world bar형태로 출력
        const div = d3.select('#chart-container');
        div.html("Hello worlddd!!");

        const node = d3.create('span').style('color', 'white')
            .style('background-color', 'black')
            .html('hello world!!!!').node();

        element.appendChild(node);

        const span = d3.create('span');
        span.style('color', 'white');
        span.style('background-color','steelblue');
        span.html('welcome D3!!');

        element.appendChild(span.node());
        // <<< hello world bar형태로 출력

        // >>> 바 형태 출력
        let data = new Array(4,8,15,16,23,42);
        let x = d3.scaleLinear().domain([0, d3.max(data)]).range([0,420]);

        // >>> chapter 2 & 3
        let width = 420;
        let y = d3.scaleBand().domain(d3.range(data.length)).range([0, 40 * data.length]);
        let z = d3.scaleOrdinal().domain(["apples", "limes", "blueberries"]).range(["red", "green", "blue"]);

        const svg = d3.create("svg")
            .attr("width", width)
            .attr("height", y.range()[1])
            .attr("font-family", "sans-serif")
            .attr("font-size", "15")
            .attr("text-anchor", "end");

        const bar = svg.selectAll("g")
            .data(data)
            .join("g")
            .attr("transform", (d, i) => `translate(0,${y(i)})`);

        bar.append('rect')
            .attr('fill', 'steelblue')
            .attr('width', x)
            .attr('height', y.bandwidth() - 1);

        bar.append('text')
            .attr('fill', 'white')
            .attr('x', d=>x(d) - 10)
            .attr('y', (y.bandwidth() - 1) / 2)
            .attr('dy', '0.35em')
            .text(d => d);
        // <<< chapter 2 & 3

        // >>> chapter 1
        const div2 = d3.create("div")
            .style("font", "10px sans-serif")
            .style("text-align", "right")
            .style("color", "white");

        const div3 = d3.create("div")
            .style("font", "10px sans-serif")
            .style("text-align", "right")
            .style("color", "white");

        div3.selectAll('div')       // Define the initial (empty) selection for the bars.
            .data(data)             // Bind this selection to the data (computing enter, update and exit).
            .join("div")            // Join the selection and the data, appending the entering bars.
            .style("background", "steelblue")
            .style("padding", "10px")
            .style("margin", "1px")
            .style("width", d => `${x(d)}px`)
            .text(d => d);
        // <<< chapter 1

        element.appendChild(svg.node());
    });

</script>

<div id="chart-container" bind:this={element}></div>
