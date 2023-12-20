package film.search.filmsearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import film.search.filmsearch.utils.AnimationHelper
import film.search.filmsearch.databinding.FragmentCollectionBinding

// Empty collection fragment
class CollectionFragment : Fragment() {
    private lateinit var binding: FragmentCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCollectionBinding.inflate(layoutInflater)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.root, requireActivity(), 3)
        return binding.root
    }

}
