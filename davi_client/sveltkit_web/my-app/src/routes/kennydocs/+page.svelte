<script>
    let moduleTest = "";
    let convert = "";
    let testStr = "";

    let clothTest = "";
    let convert2 = "";

    let content = "";

    let inputTextArea = "";

    let resultVisible = false;

    function showResult(){
        console.log(inputTextArea);
        resultVisible = true;
        console.log("show Result");
        moduleTest = "모듈 테스트";
        convert = "(찾아 바꾸기 -> 대문자화 -> 목차화";
        testStr = "- TESTTESTTEST";

        clothTest = "의류 사이즈 변환 테스트";
        convert2 = "(행 복사 -> 영역 분할 -> 단위 변환 -> 영역 병합 -> 쉼표를 탭으로 -> 행열 뒤집기)";

        content = "\tKR\tUSA\tJP\tUK\tFR\tEU\n" +  
        "\t90\t85-90\tS\t0\t40    \t\t44-46\n" +
        "\t95\t90-95\tM\t1\t42, 44\t\t46\n" + 
        "\t100\t95-100\tL\t2\t46, 48\t\t48\n" +
        "\t105\t100-105\tL\t3\t50, 52\t\t50";

        console.log(content);
    }
</script>

<div class="form-group" style="width:800px; margin:30px 0 0 30px">
    <label for="comment">
        <strong>입력</strong>
    </label>
    <textarea class="form-control" rows="5" id="comment" bind:value={inputTextArea}></textarea>
    <button class="btn btn-primary mt-4" on:click={showResult}>Convert</button>
</div>
<div style="width:800px; margin:30px 0 0 30px">
    {#if resultVisible}
    <h4>결과</h4>
    <p>{moduleTest}</p>
    <p>{convert}</p>
    <p>{testStr}</p>
    <p>{testStr}</p>
    <p>{testStr}</p>
    <p></p>
    <p>{clothTest}</p>
    <p>{convert2}</p>
    <p style="white-space:pre">{content}</p>
    <p><br/></p>
    <h4>Download document</h4>
    <div class="d-flex">
        <button class="btn btn-outline-primary mr-3">test.docs</button>
        <button class="btn btn-outline-success">test.pdf</button>
    </div>
    {/if}
</div>