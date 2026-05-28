<script setup lang="ts">

  definePageMeta({
    layoutProps: { title: true }
  });

  onMounted( () => {

    useAuth().me().catch( () => {
      useToast().add({
        title: 'Login to access to more features',
        icon: 'i-lucide-message-circle-warning'
      })
    })

  })

</script>

<template>
  <UContainer>
    <UCard
      class="relative -translate-y-1/11 md:-translate-y-1/3 shadow-[0_4px_8px_0_#00000025] flex flex-col bg-[#EDEDED] p-4 px-6 pb-8 rounded-xs"
      :ui="{ body: 'p-0! homeGrid', header: '!border-b-0 !border-none !ring-0 !shadow-none' }">
      <template #default>

        <UserPopularList class="popular"/>
        <UserRecentList class="recent"/>
        <UserSearchPanel class="search"/>

      </template>
    </UCard>
  </UContainer>
</template>

<style lang="scss">

  .homeGrid {

    display: grid;
    grid-template-areas:
      "popular search"
      "recent search";

    @media screen and (max-width: 768px) {
      grid-template-areas:
        "popular"
        "recent"
        "search";
      grid-template-columns: 1fr !important;
      row-gap: 1.5rem;
    }

    grid-template-columns: 2fr 3fr;
    column-gap: 1.5rem;

  }

  .popular { grid-area: popular; }
  .recent { grid-area: recent; }
  .search { grid-area: search; }

</style>
