<script>
    import Header from "../layout/common/header/header.svelte";
    import Navbar from "../layout/common/navbar/navbar.svelte";
    import Footer from "../layout/common/footer/footer.svelte";

    export let data;
    console.log("data : " + data.refreshTokenFlag);
</script>
<svelte:head>
   <title>SvelteKit Web</title> 
</svelte:head>

<body class="home-body">
    <div class="wrap">
        <!-- header -->
        <Header/>
        
        <!-- navbar -->
        <Navbar refreshTokenFlag={data.refreshTokenFlag}/>
        
        <!--body-->
        <slot/>

        <!-- footer -->
        <Footer/>
    </div>
</body>

<style>
    .home-body{
        height: 100%;
        width: 100%;
    }

    .wrap{
        position: relative;
        min-height: 100%;
        padding-bottom: 250px;
    }
</style>